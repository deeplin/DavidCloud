using DavidCloud.Handlers;
using DavidCloud.Models;
using DavidCloud.Utils;
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
            return 32;
        }

        protected override void ParseCommand(ConsolePacket ladderPacket)
        {
            string consoleId = Encoding.ASCII.GetString(ladderPacket.Body, 0, 16);
            //string password = Encoding.ASCII.GetString(ladderPacket.Body, 16, 24);
            string longlat = Encoding.ASCII.GetString(ladderPacket.Body, 40, 24);

            IUnityContainer container = UnityConfig.GetConfiguredContainer();
            DavidConsole davidConsole = container.Resolve<DavidConsole>();

            davidConsole.ConsoleId = consoleId.Replace("\0", "");
            davidConsole.EndPoint = PacketUtil.IPEndPointToString(ladderPacket.RemoteEndPoint);
            davidConsole.LoginTime = DateTime.Now.ToString("yyyy-MM-dd HH:mm:ss");
            davidConsole.HeartBeatTime = DateTime.Now.ToString("yyyy-MM-dd HH:mm:ss");

            //解析经纬度
            try
            {
                string[] locations = longlat.Split(',');
                davidConsole.Longitude = Double.Parse(locations[0]);
                davidConsole.Latitude = Double.Parse(locations[1]);
            }
            catch (Exception)
            {
                davidConsole.Longitude = null;
                davidConsole.Latitude = null;
            }

            ConsoleLogHandler.Log(ConsoleLogHandler.DOUBLE_IN, String.Format("Login: Id={0}", davidConsole.ConsoleId));
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
