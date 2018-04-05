using DavidCloud.Models;
using DotNetty.Transport.Channels;
using System;

namespace DavidCloud.Strategy
{
    abstract class BaseStrategy : IStrategy
    {
        public void Execute(IChannelHandlerContext ctx, ConsolePacket ladderPacket)
        {
            CheckBodyLength(ladderPacket);
            ParseCommand(ladderPacket);
            BuildResponse(ladderPacket);
            ctx.WriteAndFlushAsync(ladderPacket);
            PostExecute(ctx, ladderPacket);
        }

        protected abstract int GetBodyLength();
        private void CheckBodyLength(ConsolePacket ladderPacket)
        {
            int length = GetBodyLength();
            if (length >= 0 && ladderPacket.Length != length)
            {
                throw new Exception(String.Format("Illegal {0}:{1} message length. {2}",
                    ladderPacket.MessageType, ladderPacket.Operation, ladderPacket.Body.Length));
            }
        }
        protected abstract void ParseCommand(ConsolePacket ladderPacket);
        protected abstract void BuildResponse(ConsolePacket ladderPacket);
        protected abstract void PostExecute(IChannelHandlerContext ctx, ConsolePacket ladderPacket);
    }
}
