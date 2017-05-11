using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace DavidCloud.Models
{
    public class Hospital
    {
        [Key]
        public int HospitalId { get; set; }
        public string HospitalName { get; set; }

        [MaxLength(1024)]
        public string HospitalAddress { get; set; }

        [MaxLength(32)]
        public string ContactName { get; set; }

        [MaxLength(32)]
        public string ContactPhone { get; set; }

        [MaxLength(32)]
        public string ContactMobile { get; set; }
    }
}
