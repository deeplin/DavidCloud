using DavidCloud.Controllers;
using DavidCloud.Utils;
using Microsoft.Practices.Unity;
using NLog;
using System;
using System.Threading.Tasks;

namespace DavidCloud
{
    class Program
    {
        private static readonly Logger logger = LogManager.GetCurrentClassLogger();

        public static void Main()
        {
            SystemHelper.SetConsoleLogger();

            try
            {
                Start().Wait();
                Console.ReadLine();
                Stop().Wait();
            }
            catch (Exception ex)
            {
                logger.Error(ex.InnerException);
                Console.ReadLine();
            }
        }

        public static async Task Start()
        {
            IUnityContainer container = UnityConfig.GetConfiguredContainer();
            ConsoleControl consoleControl = container.Resolve<ConsoleControl>();
            await Task.Run(() => consoleControl.Start(ServerSetting.LadderPort));

        }

        public static async Task Stop()
        {
            IUnityContainer container = UnityConfig.GetConfiguredContainer();
            ConsoleControl consoleControl = container.Resolve<ConsoleControl>();
            await Task.Run(() => consoleControl.Stop());
        }
    }
}
