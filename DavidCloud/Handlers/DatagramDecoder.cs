using DavidCloud.Models;
using DavidCloud.Utils;
using DotNetty.Buffers;
using DotNetty.Codecs;
using DotNetty.Transport.Channels;
using DotNetty.Transport.Channels.Sockets;
using Microsoft.Practices.Unity;
using System;
using System.Collections.Generic;
using System.Net;
using System.Text;

namespace DavidCloud.Handlers
{
    class DatagramDecoder : MessageToMessageDecoder<DatagramPacket>
    {
        protected override void Decode(IChannelHandlerContext context, DatagramPacket packet, List<object> output)
        {
            IByteBuffer byteBuffer = packet.Content;

            //数据包长度初始检查
            int packetLength = byteBuffer.ReadableBytes;
            if (packetLength < Constant.PROTOCOL_HEADER_LENGTH)
            {
                throw new Exception("Message incompleted.");
            }

            //检查Protocol String
            byte[] protocolStringBytes = new byte[Constant.PROTOCOL_STRING.Length];
            byteBuffer.ReadBytes(protocolStringBytes);
            string protocolString = Encoding.ASCII.GetString(protocolStringBytes, 0, Constant.PROTOCOL_STRING.Length);
            if (!protocolString.Equals(Constant.PROTOCOL_STRING))
            {
                throw new Exception("Mismatch protocol string.");
            }

            IUnityContainer container = UnityConfig.GetConfiguredContainer();
            ConsolePacket consolePacket = container.Resolve<ConsolePacket>();

            //读取数据报头
            byteBuffer.ReadBytes(consolePacket.Header);
            //检查数据报长度 不包含报文头
            if (consolePacket.Length != packetLength - Constant.PROTOCOL_HEADER_LENGTH)
            {
                throw new Exception("Length error.");
            }

            //读取body
            consolePacket.Body = new byte[packetLength - Constant.PROTOCOL_HEADER_LENGTH];
            byteBuffer.ReadBytes(consolePacket.Body);

            consolePacket.RemoteEndPoint = packet.Sender as IPEndPoint;

            ConsoleLogHandler.LogPacket(ConsoleLogHandler.IN, consolePacket);

            output.Add(consolePacket);
        }
    }
}
