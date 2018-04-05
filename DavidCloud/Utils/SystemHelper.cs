using DotNetty.Common.Internal.Logging;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.Logging.Console;
using System;

namespace DavidCloud.Utils
{
    static class SystemHelper
    {
        static SystemHelper()
        {
            Configuration = new ConfigurationBuilder()
                .SetBasePath(ProcessDirectory)
                .AddJsonFile("AppSettings.json")
                .Build();
        }

        public static string ProcessDirectory
        {
            get
            {
                return AppContext.BaseDirectory;
            }
        }

        public static IConfigurationRoot Configuration { get; }

        public static void SetConsoleLogger()
        {
            ConsoleLoggerProvider consoleLoggerProvider = new ConsoleLoggerProvider((s, level) => true, false);
            InternalLoggerFactory.DefaultFactory.AddProvider(consoleLoggerProvider);
        }
    }
}
