using System;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Forms;

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

        public MainWindow()
        {
            InitializeComponent();
        }
    
        

        private async void sendKeys()
        {
            await Task.Delay(3000);
            SendKeys.SendWait("^(a)");
        }

        private void delaySlider_ValueChanged(object sender, RoutedPropertyChangedEventArgs<double> e)
        {
            delay = (int)(e.NewValue * 100);
            delayText.Text = $"Delay: {delay}ms";
        }
    }
}
