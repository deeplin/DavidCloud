using Domain.Entities;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Domain.Contexts
{
    public class ConsoleRepository
    {
        private ConsoleContext consoleContext = new ConsoleContext();

        public DavidConsole GetConsoleById(string consoleId)
        {
            lock (this)
            {
                var consoles = consoleContext.Consoles.
                    Where(console => console.ConsoleId.Equals(consoleId));
                if (consoles.Count() > 0)
                {
                    return consoles.First();
                }
                return null;
            }
        }

        public DavidConsole GetConsoleByEndPoint(string endPoint)
        {
            lock (this)
            {
                var ladders = from ladder in consoleContext.Consoles
                              where ladder.EndPoint.Equals(endPoint)
                              select ladder;
                if (ladders.Count() > 0)
                {
                    return ladders.First();
                }
                return null;
            }
        }

        public void Save(DavidConsole console)
        {
            lock (this)
            {
                DavidConsole dbEntry = GetConsoleById(console.ConsoleId);
                if (dbEntry != null)
                {
                    dbEntry.Latitude = console.Latitude;
                    dbEntry.Longitude = console.Longitude;
                    dbEntry.Address = console.Address;
                    dbEntry.Describe = console.Describe;
                    dbEntry.EndPoint = console.EndPoint;
                    dbEntry.LoginTime = console.LoginTime;
                    dbEntry.HeartBeatTime = console.HeartBeatTime;
                }
                else
                {
                    consoleContext.Consoles.Add(console);
                }
                consoleContext.SaveChanges();
            }
        }

        public void Update(string endPoint, Analog analog)
        {
            lock (this)
            {
                DavidConsole dbEntry = GetConsoleByEndPoint(endPoint);
                if (dbEntry != null)
                {
                    if(dbEntry.Analog == null)
                    {
                        dbEntry.Analog = new Analog();
                    }
                    dbEntry.Analog.Copy(analog);

                    consoleContext.SaveChanges();
                }
            }
        }
    }
}
