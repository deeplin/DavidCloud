using DavidCloud.Utils;
using System;
using System.Net;

namespace DavidCloud.Models
{
    public class ConsolePacket
    {
        //<Protocol String><Mode><Operation><Thread><Length><CRC><Data.......>
        //      6B           1B      1B        2B      2B    4B    Length B
        public byte[] Header { get; set; }

        public byte Mode
        {
            get { return Header[0]; }
            set { Header[0] = value; }
        }

        public byte Operation
        {
            get { return Header[1]; }
            set { Header[1] = value; }
        }

        public ushort Thread
        {
            get
            {
                return (ushort)(Header[2] * 256 + Header[3]);
            }
            set
            {
                Header[2] = (byte)(value >> 8);
                Header[3] = (byte)(value & 0xff);
            }
        }

        public ushort Length
        {
            get
            {
                return (ushort)(Header[4] * 256 + Header[5]);
            }
            set
            {
                Header[4] = (byte)(value >> 8);
                Header[5] = (byte)(value & 0xff);
            }
        }

        public uint CRC
        {
            get
            {
                return (uint)(Header[6] * 256 * 256 * 256
                + Header[7] * 256 * 256
                + Header[8] * 256
                + Header[9]
                );
            }
            set
            {
                Header[6] = (byte)(value >> 24);
                Header[7] = (byte)(value >> 16);
                Header[8] = (byte)(value >> 8);
                Header[9] = (byte)(value & 0xff);
            }
        }

        public byte Encryption
        {
            get
            {
                return (byte)(Mode >> 5);
            }
            set
            {
                Mode = (byte)((Mode & 0x1f) | (value << 5));
            }
        }

        public byte MessageType
        {
            get
            {
                return (byte)((Mode & 0x1f) >> 2);
            }

            set
            {
                Mode = (byte)((Mode & (0xE0 + 0x3) | ((value & 0x07) << 2)));
            }
        }

        public byte Sequence
        {
            get
            {
                return (byte)(Mode & 0x03);
            }
            set
            {
                Mode = (byte)((Mode & 0xFC) | (value & 0x03));
            }
        }

        public byte[] Body
        {
            get; set;
        }

        public IPEndPoint RemoteEndPoint { get; set; }

        public ConsolePacket()
        {
            Header = new byte[Constant.PROTOCOL_HEADER_LENGTH - Constant.PROTOCOL_STRING.Length];
            Encryption = 0;
            Operation = 0;
        }

        public static ushort GenerateThread()
        {
            int random = new Random().Next();
            return (ushort)(random & 0xFFFF);
        }
    }
}
