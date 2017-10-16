using System.ComponentModel.DataAnnotations;

namespace DavidCloud.Models
{
    class DavidConsole
    {
        public DavidConsole()
        {
        }

        #region Ladder
        [Key]
        [StringLength(128)]
        public string ConsoleId { get; set; }

        public ushort State { get; set; }

        public ushort Position { get; set; }

        public ushort Alert { get; set; }

        public ushort MotorCurrent { get; set; }

        public double? Latitude { get; set; }
        public double? Longitude { get; set; }
        #endregion

        #region Packet
        [StringLength(32)]
        public string EndPoint { get; set; }

        [StringLength(32)]
        public string LoginTime { get; set; }

        [StringLength(32)]
        public string HeartBeatTime { get; set; }
        #endregion
    }
}
