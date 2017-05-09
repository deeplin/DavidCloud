using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace DavidCloud.Utils
{
    static class ByteUtil
    {
        public static byte[] GetRandom(int size)
        {
            byte[] result = new byte[size];
            Random random = new Random();
            random.NextBytes(result);
            return result;
        }
    }
}
