using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Text;
using System.Threading.Tasks;

namespace DavidCloud.Packets
{
    class Packet
    {
        public byte[] Data { get; set; }

        public int Length { get; set; }

        public IPEndPoint EndPoint { get; set; }
    }
}
