using DavidCloud.Networks;
using DavidCloud.Utils;
using Microsoft.Practices.Unity;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;

namespace DavidCloud.Views.Manager
{
    /// <summary>
    /// Interaction logic for ServiceManager.xaml
    /// </summary>
    public partial class ServiceManager : Page
    {
        private readonly NLog.Logger mLogger = NLog.LogManager.GetCurrentClassLogger();

        public ServiceManager()
        {
            InitializeComponent();
            //Register IServiceManager
            IUnityContainer container = UnityConfig.GetConfiguredContainer();
            container.RegisterInstance<ServiceManager>(this);

            StartServer();
        }

        private void Button_Click(object sender, RoutedEventArgs e)
        {
            Button btn = e.Source as Button;
            string name = btn.Name;
            switch (name)
            {
                case ("startServer"):
                    StartServer();
                    break;
                case ("stopServer"):
                    StopServer();
                    break;
                case ("clearLog"):
                    display.Text = "";
                    break;
            }
        }

        private void StartServer()
        {
            try
            {
                startServer.IsEnabled = false;
                stopServer.IsEnabled = true;
                IUnityContainer container = UnityConfig.GetConfiguredContainer();
                IConsoleServer consoleServer = container.Resolve<IConsoleServer>();
                consoleServer.Start();
                //IMobileServer mobileServer = container.Resolve<IMobileServer>();
                //mobileServer.Start();
            }
            catch (Exception ex)
            {
                mLogger.Error(ex, "Initialize udp service error.");
            }
        }

        private void StopServer()
        {
            startServer.IsEnabled = true;
            stopServer.IsEnabled = false;
            IUnityContainer container = UnityConfig.GetConfiguredContainer();
            IConsoleServer consoleServer = container.Resolve<IConsoleServer>();
            consoleServer.Dispose();
            //IMobileServer mobileServer = container.Resolve<IMobileServer>();
            //mobileServer.Dispose();
        }

        private int count = 0;
        public void Display(string message)
        {
            count++;
            display.Dispatcher.Invoke(() =>
            {
                if (count > 16 * 8)
                {
                    display.Text = "";
                    count = 0;
                }
                else if (count % 16 == 0)
                {
                    display.Text += "\n\r";
                }
                display.Text += message + ": ";
            });
        }
    }
}
