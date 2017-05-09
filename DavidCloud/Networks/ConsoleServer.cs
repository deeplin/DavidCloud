using DavidCloud.Packets;
using DavidCloud.Utils;
using Microsoft.Practices.Unity;
using System;
using System.Configuration;
using System.Net;
using System.Net.Sockets;
using System.Reactive.Concurrency;
using System.Reactive.Linq;
using System.Reactive.Subjects;
using System.Threading;
using System.Threading.Tasks;

namespace DavidCloud.Networks
{
    class ConsoleServer : IConsoleServer
    {
        private UdpClient mUdpClient;
        private CancellationTokenSource mCancellationTokenSource;

        public ConsoleServer()
        {
        }

        public void Start()
        {
            int cloudServerPort = int.Parse(ConfigurationManager.AppSettings[Constant.CONSOLE_SERVER_PORT]);
            //Server IPaddress
            IPEndPoint serverEndPoint = new IPEndPoint(IPAddress.Any, cloudServerPort);
            //Initialize udpclient
            mUdpClient = new UdpClient(serverEndPoint);

            //Start listenTask in new thread
            mCancellationTokenSource = new CancellationTokenSource();
            Task.Run(() => Listen());
        }

        private void Listen()
        {
            ISubject<Packet, UdpReceiveResult> consoleClientSubject = mUdpClient.ToClientSubject(Constant.CONSOLE);

            IUnityContainer container = UnityConfig.GetConfiguredContainer();
            container.RegisterInstance<Action<Packet>>(Constant.CONSOLE, consoleClientSubject.OnNext,
                new ContainerControlledLifetimeManager());

            //Reactive extension
            ConsoleObserver consoleObserver = container.Resolve<ConsoleObserver>();
            consoleClientSubject.ObserveOn(TaskPoolScheduler.Default).Subscribe(consoleObserver,
                mCancellationTokenSource.Token);
        }

        public void Dispose()
        {
            this.Dispose(true);
            GC.SuppressFinalize(this);
        }

        ~ConsoleServer()
        {
            Dispose(false);
        }

        protected virtual void Dispose(bool disposing)
        {
            if (disposing)
            {
                if (mCancellationTokenSource != null)
                {
                    mCancellationTokenSource.Cancel();
                    mCancellationTokenSource = null;
                }
                if (mUdpClient != null)
                {
                    mUdpClient.Close();
                    mUdpClient = null;
                }
            }
        }
    }
}
