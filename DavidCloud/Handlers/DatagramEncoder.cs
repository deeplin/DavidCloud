using DavidCloud.Models;
using DavidCloud.Utils;
using DotNetty.Buffers;
using DotNetty.Codecs;
using DotNetty.Transport.Channels;
using DotNetty.Transport.Channels.Sockets;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace DavidCloud.Handlers
{
    class DatagramEncoder : MessageToMessageEncoder<ConsolePacket>
    {
        private readonly byte[] ProtocolStringBytes;

        public DatagramEncoder()
        {
            ProtocolStringBytes = Encoding.ASCII.GetBytes(Constant.PROTOCOL_STRING);
        }

        protected override void Encode(IChannelHandlerContext context, ConsolePacket ladderPacket, List<object> output)
        {
            try
            {
                IByteBuffer buffer = context.Allocator.Buffer();
                buffer.WriteBytes(ProtocolStringBytes);
                buffer.WriteBytes(ladderPacket.Header);
                buffer.WriteBytes(ladderPacket.Body);

                ConsoleLogHandler.LogPacket(ConsoleLogHandler.OUT, ladderPacket);

                output.Add(new DatagramPacket(buffer, ladderPacket.RemoteEndPoint));
            }
            catch (Exception exception)
            {
                ConsoleLogHandler.LogAll(exception);
            }
        }
    }
}
