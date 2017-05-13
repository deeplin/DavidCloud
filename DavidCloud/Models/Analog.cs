using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace DavidCloud.Models
{
    public class Analog
    {
        public string Skin { get; set; }
        public string Air { get; set; }
        public string O2 { get; set; }
        public string Hum { get; set; }

        public string SPO2 { get; set; }
        public string PR { get; set; }
        public string OccurTime { get; set; }

        public Analog()
        {
            Skin = "";
            Air = "";
            O2 = "";
            Hum = "";
            SPO2 = "";
            PR = "";
            OccurTime = "";
        }
    }
}
