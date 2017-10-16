using DavidCloud.Enums;
using DavidCloud.Models;
using DavidCloud.Strategy;
using DavidCloud.Utils;
using DotNetty.Transport.Channels;
using Microsoft.Practices.Unity;
using System;
using System.Collections.Generic;

namespace DavidCloud.Handlers
{
    class DatagramHandler : SimpleChannelInboundHandler<ConsolePacket>
    {
        private readonly IDictionary<int, IStrategy> strategyDictionary =
            new Dictionary<int, IStrategy>();

        public DatagramHandler()
        {
            IUnityContainer container = UnityConfig.GetConfiguredContainer();

            //#region Server Strategy
            //LoginStrategy loginStrategy = container.Resolve<LoginStrategy>();
            //AddStrategy(MessageTypeEnum.Login, loginStrategy);

            //HeartBeatStrategy heartBeatStrategy = container.Resolve<HeartBeatStrategy>();
            //AddStrategy(MessageTypeEnum.HeatBeat, heartBeatStrategy);
            //#endregion
        }

        private void AddStrategy(MessageTypeEnum messageType, IStrategy strategy)
        {
            AddStrategy(messageType, OperationEnum.Wave, strategy);
        }

        private void AddStrategy(MessageTypeEnum messageType, OperationEnum operation, IStrategy strategy)
        {
            strategyDictionary.Add(((int)messageType) * 256 + (int)operation, strategy);
        }

        protected override void ChannelRead0(IChannelHandlerContext ctx, ConsolePacket ladderPacket)
        {
            int index = ladderPacket.MessageType * 256 + ladderPacket.Operation;
            IStrategy strategy = strategyDictionary[index];
            strategy.Execute(ctx, ladderPacket);
        }

        public override void ChannelReadComplete(IChannelHandlerContext context) => context.Flush();

        public override void ExceptionCaught(IChannelHandlerContext context, Exception exception)
        {
            ConsoleLogHandler.LogAll(exception);
        }
    }
}
