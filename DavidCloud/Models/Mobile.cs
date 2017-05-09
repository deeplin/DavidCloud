using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Runtime.Serialization;
using System.Text;
using System.Threading.Tasks;

namespace DavidCloud.Models
{
    [DataContract]
    class Mobile
    {
        [DataMember(Name = "MI")]
        public String MobileId
        {
            get; set;
        }

        public IPEndPoint MobileEndPoint { get; private set; }

        public DateTime LoginTime
        {
            get; private set;
        }

        public void InitializeUser()
        {
            LoginTime = DateTime.Now;
        }

        public void InitializeAddress(IPEndPoint consoleEndPoint)
        {
            MobileEndPoint = consoleEndPoint;
        }
    }
}
