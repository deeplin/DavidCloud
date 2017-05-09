using System.Runtime.Serialization;

namespace DavidCloud.Models
{
    [DataContract]
    public class BaiduLocation
    {
        [DataMember(Name = "LT")]
        public double? Latitude { get; set; }
        [DataMember(Name = "LG")]
        public double? Longitude { get; set; }
        [DataMember(Name = "RD")]
        public float? Radius { get; set; }
        [DataMember(Name = "AD")]
        public string Address { get; set; }
        [DataMember(Name = "DC")]
        public string Describe { get; set; }
    }
}