using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace DavidCloud.Utils
{
    class StringUtil
    {
        public static String ByteToHexString(byte[] src, int start, int length)
        {
            if (src == null || src.Length < length + start || length <= 0)
            {
                return null;
            }

            StringBuilder stringBuilder = new StringBuilder();
            for (int index = start; index < start + length; index++)
            {
                int data = src[index] & 0xFF;
                
                String hv = data.ToString("X");
                if (hv.Length < 2)
                {
                    stringBuilder.Append(0);
                }
                stringBuilder.Append(hv + " ");
            }
            return stringBuilder.ToString();
        }

    }
}
