using System;
using System.Net;
using System.Text;

namespace DavidCloud.Utils
{
    class PacketUtil
    {
        public static string GetString(byte[] data, int index, int length)
        {
            string value = Encoding.ASCII.GetString(data, index, length);
            index += length + 1;
            return value;
        }

        public static string GetString(byte[] data, ref int index)
        {
            byte length = data[index];
            string value = Encoding.ASCII.GetString(data, index + 1, length);
            index += length + 1;
            return value;
        }

        public static Int32 GetInteger(byte[] data, ref int index)
        {
            int result = data[index];
            result = result * 256 + data[index + 1];
            result = result * 256 + data[index + 2];
            result = result * 256 + data[index + 3];
            index += 4;
            return result;
        }

        public static ushort GetUnsignedShort(byte[] data, ref int index)
        {
            ushort result = data[index];
            result = (ushort)(result * 256 + data[index + 1]);
            index += 2;
            return result;
        }

        public static string IPEndPointToString(IPEndPoint endPoint)
        {
            return String.Format("{0}:{1}", endPoint.Address.ToString(), endPoint.Port);
        }

        public static IPEndPoint StringToIPEndPoint(string endPoint)
        {
            String[] pair = endPoint.Split(':');
            String address = pair[pair.Length - 2];
            String port = pair[pair.Length - 1];
            return new IPEndPoint(IPAddress.Parse(address), Int32.Parse(port));
        }
    }
}
