using System.ComponentModel.DataAnnotations;

namespace Domain.Entities
{
    public class DavidConsole
    {
        #region Console
        [Key]
        [StringLength(128)]
        public string ConsoleId { get; set; }
        public double? Latitude { get; set; }
        public double? Longitude { get; set; }
        [StringLength(128)]
        public string Address { get; set; }
        [StringLength(128)]
        public string Describe { get; set; }
        #endregion

        #region Packet
        [StringLength(32)]
        public string EndPoint { get; set; }

        [StringLength(32)]
        public string LoginTime { get; set; }

        [StringLength(32)]
        public string HeartBeatTime { get; set; }
        #endregion

        public virtual Analog Analog { get; set; }
    }
}
