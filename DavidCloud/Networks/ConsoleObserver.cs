using DavidCloud.Controls;
using DavidCloud.Packets;
using DavidCloud.Utils;
using DavidCloud.Views.Manager;
using Microsoft.Practices.Unity;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Net;
using System.Net.Sockets;
using System.Reactive.Linq;
using System.Text;
using System.Threading.Tasks;

namespace DavidCloud.Networks
{
    class ConsoleObserver : IObserver<UdpReceiveResult>
    {
        private readonly NLog.Logger mLogger = NLog.LogManager.GetCurrentClassLogger();
        private readonly Action<Packet> SendUdpPacket;

        public ConsoleObserver()
        {
            IUnityContainer container = UnityConfig.GetConfiguredContainer();
            SendUdpPacket = container.Resolve<Action<Packet>>(Constant.CONSOLE);
        }

        public void OnNext(UdpReceiveResult receiveResult)
        {
            try
            {
                //Decode packet buffer
                byte[] datagram = receiveResult.Buffer;
                ConsoleRequest request = ConsoleRequest.Parse(datagram);
                {
                    IUnityContainer container = UnityConfig.GetConfiguredContainer();
                    ServiceManager serviceManager = container.Resolve<ServiceManager>();
                    serviceManager.Display("C" + request.MessageType);
                }
                switch (request.MessageType)
                {
                    case ConsoleRequest.LOGIN_MESSAGE:
                        {
                            IPEndPoint consoleEndPoint = receiveResult.RemoteEndPoint;

                            IUnityContainer container = UnityConfig.GetConfiguredContainer();
                            ConsoleControl consoleControl = container.Resolve<ConsoleControl>();

                            Packet response = consoleControl.Login(request.Datagram, consoleEndPoint);
                            SendUdpPacket(response);
                        }
                        break;
                    case ConsoleRequest.HEART_BEAT_MESSAGE:
                        {
                            IPEndPoint consoleEndPoint = receiveResult.RemoteEndPoint;

                            IUnityContainer container = UnityConfig.GetConfiguredContainer();
                            ConsoleControl consoleControl = container.Resolve<ConsoleControl>();

                            Packet response = consoleControl.HeartBeat(request.Datagram, consoleEndPoint);
                            SendUdpPacket(response);
                        }
                        break;
                    case ConsoleRequest.LOCATION_MESSAGE:
                        {
                            IPEndPoint consoleEndPoint = receiveResult.RemoteEndPoint;

                            IUnityContainer container = UnityConfig.GetConfiguredContainer();
                            ConsoleControl consoleControl = container.Resolve<ConsoleControl>();

                            Packet response = consoleControl.Location(request.Datagram, consoleEndPoint);
                            SendUdpPacket(response);
                        }
                        break;
                }
            }
            catch (Exception e)
            {
                mLogger.Error(e, "Parse udp packet error.");
            }
        }

        public void OnError(Exception e)
        {
            mLogger.Error(e, "Receive udp packet error.");
        }

        public void OnCompleted()
        {
        }
    }
}
