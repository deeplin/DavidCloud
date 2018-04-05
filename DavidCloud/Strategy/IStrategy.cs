using DavidCloud.Models;
using DotNetty.Transport.Channels;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace DavidCloud.Strategy
{
    public interface IStrategy
    {
        void Execute(IChannelHandlerContext ctx, ConsolePacket ladderPacket);
    }
}
