using NLog;
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
using System.Windows.Shapes;

namespace DavidCloud.Views.Main
{
    /// <summary>
    /// Interaction logic for MainWindow.xaml
    /// </summary>
    public partial class MainWindow : Window
    {
        private readonly Logger mLogger = LogManager.GetCurrentClassLogger();

        public MainWindow()
        {
            InitializeComponent();
        }

        //Start all services
        private void Window_Initialized(object sender, EventArgs e)
        {
            try
            {
                //using (DavidDatabase davidCloud = new DavidDatabase())
                //{
                //    davidCloud.Database.CreateIfNotExists();
                //}
            }
            catch (Exception ex)
            {
                mLogger.Error(ex, "Initialize error.");
                MessageBox.Show(ex.Message + " Initialization Error. Server quited. Please check the event viewer.");
                Application.Current.Shutdown();
            }
        }

        private Button mOldButton = new Button();
        private void Button_Click(object sender, RoutedEventArgs e)
        {
            Button button = e.Source as Button;
            button.Foreground = Brushes.Red;
            mOldButton.Foreground = Brushes.Blue;
            mOldButton = button;
            Uri uri = new Uri(button.Tag.ToString(), UriKind.Relative);
            detailFrame.Source = new Uri(button.Tag.ToString(), UriKind.Relative);
        }
    }
}
