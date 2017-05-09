using DavidCloud.Packets;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace DavidCloud.Controls
{
    static class MessageBuilder
    {
        public static byte[] GetBody(byte[] data, int index)
        {
            int length = data.Length - ConsoleRequest.PROTOCOL_HEADER_LENGTH - index;
            byte[] body = new byte[length];
            Buffer.BlockCopy(data, ConsoleRequest.PROTOCOL_HEADER_LENGTH + index, body, 0, length);
            return body;
        }

        public static byte[] GetToken(byte[] data)
        {
            byte[] body = new byte[ConsoleRequest.TOKEN_LENGTH];
            Buffer.BlockCopy(data, ConsoleRequest.PROTOCOL_HEADER_LENGTH, body, 0, ConsoleRequest.TOKEN_LENGTH);
            return body;
        }

        public static string GetNext(byte[] data, ref int index)
        {
            byte length = data[index];
            string value = Encoding.UTF8.GetString(data, index + 1, length);
            index += length + 1;
            return value;
        }
    }
}
