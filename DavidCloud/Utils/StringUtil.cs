using System;
using System.Text;

namespace DavidCloud.Utils
{
    class StringUtil
    {
        public static String ByteToHexString(byte[] src)
        {
            return ByteToHexString(src, 0, src.Length);
        }

        public static String ByteToHexString(byte[] src, int start, int length)
        {
            if (src == null || src.Length < length + start || length <= 0)
            {
                return null;
            }

            StringBuilder stringBuilder = new StringBuilder();
            for (int index = start; index < start + length; index++)
            {
                String text = src[index].ToString("X2");
                stringBuilder.Append(text).Append(" ");
            }
            return stringBuilder.ToString();
        }
    }
}
