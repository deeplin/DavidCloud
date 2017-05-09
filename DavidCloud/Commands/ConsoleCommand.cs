using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace DavidCloud.Commands
{
    class ConsoleCommand
    {
        public const int PROTOCOL_HEADER_LENGTH = 16;
        public const string PROTOCOL_STRING = "HYIOT1";

        private byte[] requestBytes;

        bool encryption;
        byte operation;
        IList<string> stringFields;
        protected byte[] body;

        short thread;

        public ConsoleCommand()
        {
            encryption = false;
            operation = 0;
            body = null;
            stringFields = new List<string>();

            int random = new Random().Next();
            thread = (short)(random & 0xFFFF);
        }

        private byte GetMode()
        {
            byte mode = 0;
            if (encryption)
                mode = 32;
            mode += MessageType;
            mode <<= 2;
            mode += GetSequence();
            return mode;
        }

        public byte GetSequence()
        {
            return 0;
        }

        public byte MessageType
        {
            get; set;
        }

        public short GetThread()
        {
            return thread;
        }

        public void AddStringField(String field)
        {
            stringFields.Add(field);
        }

        public byte[] GetRequest()
        {
            int dataLength = 0;
            if (body != null)
            {
                dataLength = body.Length;
            }
            foreach (String field in stringFields)
            {
                dataLength++;
                dataLength += Encoding.UTF8.GetBytes(field).Length;
            }

            requestBytes = new byte[PROTOCOL_HEADER_LENGTH + dataLength];

            Buffer.BlockCopy(Encoding.UTF8.GetBytes(PROTOCOL_STRING), 0, requestBytes, 0, PROTOCOL_STRING.Length);
            requestBytes[6] = GetMode();
            requestBytes[7] = operation;
            short thread = GetThread();
            requestBytes[8] = (byte)(thread & 0xff);
            requestBytes[9] = (byte)(thread >> 8);
            requestBytes[10] = (byte)(dataLength & 0xff);
            requestBytes[11] = (byte)(dataLength >> 8);

            //CSC
            requestBytes[12] = 0;
            requestBytes[13] = 0;
            requestBytes[14] = 0;
            requestBytes[15] = 0;

            int index = PROTOCOL_HEADER_LENGTH;
            if (body != null)
            {
                Buffer.BlockCopy(body, 0, requestBytes, index, body.Length);
                index += body.Length;
            }
            foreach (String field in stringFields)
            {
                byte[] data = Encoding.UTF8.GetBytes(field);
                requestBytes[index] = (byte)data.Length;
                index++;
                Buffer.BlockCopy(data, 0, requestBytes, index, data.Length);
                index += data.Length;
            }

            return requestBytes;
        }
    }
}
