using DavidCloud.Handlers;
using DavidCloud.Models;
using DavidCloud.Utils;
using Domain.Contexts;
using Domain.Entities;
using DotNetty.Transport.Channels;
using Microsoft.Practices.Unity;
using System;
using System.Text;

namespace DavidCloud.Strategy
{
    class LoginStrategy : BaseStrategy
    {
        readonly byte[] successfulBody = new byte[4];
        public LoginStrategy()
        {
            successfulBody[0] = 0;
            successfulBody[1] = 0;
            successfulBody[2] = 0;
            successfulBody[3] = 0;
        }

        protected override int GetBodyLength()
        {
            return 584;
        }

        protected override void ParseCommand(ConsolePacket ladderPacket)
        {
            string consoleId = Encoding.ASCII.GetString(ladderPacket.Body, 0, 16);
            string password = Encoding.ASCII.GetString(ladderPacket.Body, 16, 32);
            string longlat = Encoding.ASCII.GetString(ladderPacket.Body, 48, 24);
            string address = Encoding.UTF8.GetString(ladderPacket.Body, 72, 256);
            string describe = Encoding.UTF8.GetString(ladderPacket.Body, 328, 256);

            IUnityContainer container = UnityConfig.GetConfiguredContainer();
            DavidConsole davidConsole = container.Resolve<DavidConsole>();

            davidConsole.ConsoleId = consoleId.Replace("\0", "");
            davidConsole.Address = address.Replace("\0", "");
            davidConsole.Describe = describe.Replace("\0", "");
            davidConsole.EndPoint = PacketUtil.IPEndPointToString(ladderPacket.RemoteEndPoint);
            davidConsole.LoginTime = DateTime.Now.ToString("yyyy-MM-dd HH:mm:ss");
            davidConsole.HeartBeatTime = DateTime.Now.ToString("yyyy-MM-dd HH:mm:ss");

            double longitude = 0;
            double latitude = 0;
            //解析经纬度
            try
            {
                string[] locations = longlat.Split(',');
                if (Double.Parse(locations[0]) > 0)
                {
                    longitude = Double.Parse(locations[0]);
                }
                if (Double.Parse(locations[1]) > 0)
                {
                    latitude = Double.Parse(locations[1]);
                }
            }
            catch (Exception)
            {
            }
            davidConsole.Longitude = longitude;
            davidConsole.Latitude = latitude;

            ConsoleLogHandler.Log(ConsoleLogHandler.DOUBLE_IN, String.Format("Login: Id={0} LG={1} LT={2}",
                davidConsole.ConsoleId, davidConsole.Longitude, davidConsole.Latitude));

            ConsoleRepository consoleRepository = container.Resolve<ConsoleRepository>();
            consoleRepository.Save(davidConsole);
        }

        protected override void BuildResponse(ConsolePacket ladderPacket)
        {
            ladderPacket.Sequence = 1;
            ladderPacket.Length = (ushort)successfulBody.Length;
            ladderPacket.Body = successfulBody;
        }

        protected override void PostExecute(IChannelHandlerContext ctx, ConsolePacket ladderPacket)
        {
        }
    }
}
