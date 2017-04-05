using DavidCloud.Network;
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
    public partial class ServiceManager : IServiceManager
    {
        private readonly NLog.Logger mLogger = NLog.LogManager.GetCurrentClassLogger();

        public ServiceManager()
        {
            InitializeComponent();
            //Register IServiceManager
            IUnityContainer container = UnityConfig.GetConfiguredContainer();
            container.RegisterInstance<IServiceManager>(this);

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
            startServer.IsEnabled = false;
            stopServer.IsEnabled = true;
            try
            {
                IUnityContainer container = UnityConfig.GetConfiguredContainer();
                IUdpServer udpServer = container.Resolve<IUdpServer>();
                udpServer.Start();
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
            IUdpServer udpServer = container.Resolve<IUdpServer>();
            udpServer.Stop();
        }

        private int count = 0;
        public void Display(string message)
        {
            count++;
            display.Dispatcher.Invoke(() =>
            {
                if (count >= 8)
                {
                    display.Text += "\n\r";
                    count = 0;
                }
                display.Text += message + ";";
            });
        }
    }
}
