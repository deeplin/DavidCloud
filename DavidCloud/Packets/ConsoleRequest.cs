using DavidCloud.Utils;
using Microsoft.Practices.Unity;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace DavidCloud.Packets
{
    class ConsoleRequest : IDatagram
    {
        //<Protocol String><Mode><Operation><Thread><Length><CRC><Data.......>
        //    6B            1B       1B       2B       2B    4B     Length B
        public const string PROTOCOL_STRING = "HYIOT1";
        public const int PROTOCOL_HEADER_LENGTH = 16;
        public const int REQUEST_MAX_LENGTH = 10240;
        public const int TOKEN_LENGTH = 8;

        public const int LOGIN_MESSAGE = 0;
        public const int LOCATION_MESSAGE = 5;
        public const int ALERT_MESSAGE = 6;
        public const int ANALOG_MESSAGE = 7;

        public bool Encryption { get; set; }

        public byte MessageType { get; set; }

        public byte Sequence { get; set; }

        public byte Operation { get; set; }

        public ushort Thread { get; set; }

        public int Length { get; set; }

        public int CRC { get; set; }

        public byte[] Datagram { get; set; }

        public byte BuildMode()
        {
            byte result = (byte)(Encryption ? 1 : 0 << 5);
            result += (byte)(MessageType << 2);
            result += Sequence;
            return result;
        }

        //解析Request
        public static ConsoleRequest Parse(byte[] datagram)
        {
            IUnityContainer container = UnityConfig.GetConfiguredContainer();
            ConsoleRequest request = container.Resolve<ConsoleRequest>();

            //<Protocol String><Mode><Operation><Thread><Length><CRC><Data.......>
            //6B                 1B       1B       2B       2B    4B     Length B
            string protocolString = Encoding.UTF8.GetString(datagram, 0, ConsoleRequest.PROTOCOL_STRING.Length);
            if (!protocolString.Equals(ConsoleRequest.PROTOCOL_STRING))
            {
                throw new Exception("Illegal protocol string: " + protocolString);
            }

            if (datagram.Length < ConsoleRequest.PROTOCOL_HEADER_LENGTH)
            {
                throw new Exception("Illegal protocol header: " + Encoding.UTF8.GetString(datagram));
            }

            //< Mode > := < Encryption >< Message Type >< Sequence >
            //  MSB             3b          3b              2b          LSB
            byte mode = datagram[6];
            request.Encryption = ((byte)(mode >> 5)) > 0 ? true : false;
            request.MessageType = (byte)((mode & 0x1f) >> 2);
            request.Sequence = (byte)(mode & 0x3);

            request.Operation = datagram[7];

            request.Thread = (ushort)(datagram[8] + datagram[9] * 256);

            request.Length = datagram[10] + datagram[11] * 256;

            if (request.Length > ConsoleRequest.REQUEST_MAX_LENGTH)
            {
                throw new Exception("Illegal length: " + request.Length);
            }

            if (request.Length + ConsoleRequest.PROTOCOL_HEADER_LENGTH != datagram.Length)
            {
                throw new Exception("Datagram length error: length " + request.Length + " datagram length " + datagram.Length);
            }

            request.Datagram = datagram;
            return request;
        }
    }
}
