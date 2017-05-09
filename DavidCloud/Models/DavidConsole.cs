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
        [Key]
        [DataMember(Name = "CI")]
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
            BaiduLocation = new BaiduLocation();

            LoginTime = DateTime.Now.ToString("yyyy-MM-dd HH:mm:ss");
        }

        public void InitializeAddress(IPEndPoint consoleEndPoint)
        {
            ConsoleEndPoint = consoleEndPoint;
            HeartBeatTime = DateTime.Now;
        }

        public DavidConsole Clone()
        {
            IUnityContainer container = UnityConfig.GetConfiguredContainer();
            DavidConsole console = container.Resolve<DavidConsole>();
            console.DavidConsoleId = DavidConsoleId;
            console.User = User;
            console.Password = Password;
            console.DeviceModel = DeviceModel;
            console.ManufactureDate = ManufactureDate;
            console.ConsoleEndPoint = ConsoleEndPoint;
            console.LoginTime = LoginTime;
            console.BaiduLocation = BaiduLocation;
            console.TokenBase64 = TokenBase64;
            return console;
        }
    }
}
