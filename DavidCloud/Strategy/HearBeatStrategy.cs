using DavidCloud.Models;
using DavidCloud.Utils;
using Domain.Contexts;
using Domain.Entities;
using DotNetty.Transport.Channels;
using Microsoft.Practices.Unity;
using System;

namespace DavidCloud.Strategy
{
    class HeartBeatStrategy : BaseStrategy
    {
        protected override int GetBodyLength()
        {
            return 17 * 4;
        }

        protected override void ParseCommand(ConsolePacket ladderPacket)
        {
            IUnityContainer container = UnityConfig.GetConfiguredContainer();
            Analog analog = container.Resolve<Analog>();

            int index = 0;
            byte[] body = ladderPacket.Body;
            long timeHigh = PacketUtil.GetInteger(body, ref index);
            long timeLow = PacketUtil.GetInteger(body, ref index);
            long time = (timeHigh << 32) + timeLow;
            DateTime dateTime = TimeUtil.ConvertJavaDateTimeToNetTime(time);
            analog.Time = dateTime.ToString("yyyy-MM-dd HH:mm:ss");

            analog.S1A = PacketUtil.GetInteger(body, ref index);
            analog.S1B = PacketUtil.GetInteger(body, ref index);
            analog.S2 = PacketUtil.GetInteger(body, ref index);
            analog.S3 = PacketUtil.GetInteger(body, ref index);
            analog.A1 = PacketUtil.GetInteger(body, ref index);
            analog.A2 = PacketUtil.GetInteger(body, ref index);
            analog.A3 = PacketUtil.GetInteger(body, ref index);
            analog.F1 = PacketUtil.GetInteger(body, ref index);
            analog.H1 = PacketUtil.GetInteger(body, ref index);
            analog.O1 = PacketUtil.GetInteger(body, ref index);
            analog.O2 = PacketUtil.GetInteger(body, ref index);
            analog.SP = PacketUtil.GetInteger(body, ref index);
            analog.PR = PacketUtil.GetInteger(body, ref index);
            analog.PI = PacketUtil.GetInteger(body, ref index);
            analog.VU = PacketUtil.GetInteger(body, ref index);

            //显示日志
            string endPoint = PacketUtil.IPEndPointToString(ladderPacket.RemoteEndPoint);

            ConsoleRepository consoleConsoleRepository = container.Resolve<ConsoleRepository>();
            consoleConsoleRepository.Update(endPoint, analog);
        }

        protected override void BuildResponse(ConsolePacket ladderPacket)
        {
            ladderPacket.Sequence = 1;

            byte[] body = new byte[4];
            uint ticks = (uint)(DateTime.Now.Ticks / 1000);
            body[0] = (byte)(ticks >> 24);
            body[1] = (byte)(ticks >> 16);
            body[2] = (byte)(ticks >> 8);
            body[3] = (byte)(ticks);

            ladderPacket.Length = (ushort)body.Length;
            ladderPacket.Body = body;
        }

        protected override void PostExecute(IChannelHandlerContext ctx, ConsolePacket ladderPacket)
        {
        }
    }
}
