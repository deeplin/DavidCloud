using DavidCloud.Utils;
using System.Configuration;
using System.Net;
using System.Net.Sockets;
using System.Threading;
using System.Threading.Tasks;

namespace DavidCloud.Network
{
    class UdpServer : IUdpServer
    {

        private UdpClient mUdpClient;
        private CancellationTokenSource mCancellationTokenSource;

        public UdpServer()
        {
        }

        public void Start()
        {
            int cloudServerPort = int.Parse(ConfigurationManager.AppSettings[Constant.CLOUD_SERVER_PORT]);
            //Server IPaddress
            string cloudServerIP = ConfigurationManager.AppSettings[Constant.CLOUD_SERVER_IP];
            IPEndPoint serverEndPoint = new IPEndPoint(IPAddress.Parse(cloudServerIP), cloudServerPort);
            //Initialize udpclient
            mUdpClient = new UdpClient(serverEndPoint);
            //Start listenTask in new thread

            mCancellationTokenSource = new CancellationTokenSource();
            //Task.Run(() => Listen());
        }








        public void Stop()
        {
            //throw new NotImplementedException();
        }
    }
}
