using DavidCloud.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace DavidCloud.Databases
{
    class DavidConsoleRepository
    {
        private DavidDatabase davidDatabase = new DavidDatabase();

        public DavidConsole SelectFromId(String consoleId)
        {
            var davidConsoles = davidDatabase.DavidConsoles.
                Where(DavidConsole => DavidConsole.DavidConsoleId.Equals(consoleId));

            if(davidConsoles != null && davidConsoles.Count() > 0)
            {
                return davidConsoles.First();
            }
            return null;
        }

        public DavidConsole SelectFromToken(String tokenBase64)
        {
            var davidConsoles = davidDatabase.DavidConsoles.
                Where(DavidConsole => DavidConsole.TokenBase64.Equals(tokenBase64));

            if (davidConsoles.Count() > 0)
            {
                return davidConsoles.First();
            }
            return null;
        }

        public async void Save(DavidConsole davidConsole)
        {
            DavidConsole dbEntry = SelectFromId(davidConsole.DavidConsoleId);
            if(dbEntry != null)
            {
                dbEntry.User = davidConsole.User;
                dbEntry.Password = davidConsole.Password;
                dbEntry.DeviceModel = davidConsole.DeviceModel;
                dbEntry.Manufacture = davidConsole.Manufacture;
                dbEntry.ManufactureDate = davidConsole.ManufactureDate;
                dbEntry.ConsoleEndPoint = davidConsole.ConsoleEndPoint;
                dbEntry.LoginTime = davidConsole.LoginTime;
                dbEntry.HeartBeatTime = davidConsole.HeartBeatTime;
                dbEntry.BaiduLocation = davidConsole.BaiduLocation;
                dbEntry.Hospital = davidConsole.Hospital;
            } else
            {
                davidDatabase.DavidConsoles.Add(davidConsole);
            }
            await davidDatabase.SaveChangesAsync();
        }
    }
}
