using DavidCloud.Utils;
using Microsoft.Practices.Unity;
using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Net;
using System.Runtime.Serialization;
using System.Text;
using System.Threading.Tasks;

namespace DavidCloud.Models
{
    [DataContract]
    public class DavidConsole
    {

        public DavidConsole()
        {
            BaiduLocation = new BaiduLocation();
            Hospital = new Hospital();
            Alert = new Alert();
            Analog = new Analog();
        }

        [Key]
        [DataMember(Name = "CI")]
        [MaxLength(1024)]
        public String DavidConsoleId
        {
            get; set;
        }

        public string User
        {
            get; set;
        }

        public string Password
        {
            get; set;
        }

        [DataMember(Name = "DM")]
        [MaxLength(64)]
        public string DeviceModel
        {
            get; set;
        }

        [DataMember(Name = "MF")]
        public string Manufacture
        {
            get; set;
        }

        [DataMember(Name = "MD")]
        public string ManufactureDate
        {
            get; set;
        }
        [NotMapped]
        public IPEndPoint ConsoleEndPoint { get; set; }

        [DataMember(Name = "LT")]
        public String LoginTime
        {
            get; set;
        }

        public DateTime HeartBeatTime { get; set; }

        [DataMember(Name = "BL")]
        public BaiduLocation BaiduLocation { get; set; }

        public Hospital Hospital { get; set; }

        public Alert Alert { get; set; }

        public Analog Analog { get; set; }

        public String TokenBase64 { get; set; }

        public void InitializeUser(string consoleId, string user, string password,
            string deviceModel, string manufacture, string productionDate)
        {
            DavidConsoleId = consoleId;
            User = user;
            Password = password;
            DeviceModel = deviceModel;
            Manufacture = manufacture;
            ManufactureDate = productionDate;

            LoginTime = DateTime.Now.ToString("yyyy-MM-dd HH:mm:ss");
        }

        public void InitializeAddress(IPEndPoint consoleEndPoint)
        {
            ConsoleEndPoint = consoleEndPoint;
            HeartBeatTime = DateTime.Now;
        }

        //public DavidConsole Clone()
        //{
        //    IUnityContainer container = UnityConfig.GetConfiguredContainer();
        //    DavidConsole console = container.Resolve<DavidConsole>();
        //    console.DavidConsoleId = DavidConsoleId;
        //    console.User = User;
        //    console.Password = Password;
        //    console.DeviceModel = DeviceModel;
        //    console.ManufactureDate = ManufactureDate;
        //    console.ConsoleEndPoint = ConsoleEndPoint;
        //    console.LoginTime = LoginTime;
        //    console.BaiduLocation = BaiduLocation;
        //    console.TokenBase64 = TokenBase64;
        //    return console;
        //}
    }
}
