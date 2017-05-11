using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Runtime.Serialization;
using System.Text;
using System.Threading.Tasks;

namespace DavidCloud.Models
{
    [DataContract]
    public class Alert
    {
        [Key]
        public int Id { get; set; }
        public string AlertId { get; set; }
        public string OccurTime { get; set; }
        public string AlertDetail { get; set; }
    }
}
