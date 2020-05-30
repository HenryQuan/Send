using System;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Forms;
using System.Windows.Media;

namespace Send
{
    /// <summary>
    /// Interaction logic for MainWindow.xaml
    /// </summary>
    public partial class MainWindow : Window
    {
        /// <summary>
        /// The default delay is 1s, 1000ms
        /// </summary>
        private int delay = 1000;

        private string status = "NOT CONNECTED";

        public MainWindow()
        {
            InitializeComponent();

            // Setup UI
            updateStatusText();
        }
    
        

        private async void sendKeys()
        {
            await Task.Delay(3000);
            SendKeys.SendWait("^(a)");
        }

        #region IP related

        #endregion

        #region Web request

        #endregion

        #region UI update

        /// <summary>
        /// It can either be online or offline with green or red colour
        /// </summary>
        private void updateStatusText()
        {
            statusText.Text = status;
            if (status.Equals("CONNECTED"))
            {
                statusText.Foreground = Brushes.DarkGreen;
            } else
            {
                statusText.Foreground = Brushes.Red;
            }
        }

        private void delaySlider_ValueChanged(object sender, RoutedPropertyChangedEventArgs<double> e)
        {
            delay = (int)(e.NewValue * 100);
            delayText.Text = $"Delay: {delay}ms";
        }

        #endregion
    }
}
