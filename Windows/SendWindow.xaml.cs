using System.Threading.Tasks;
using System.Windows;
using System.Windows.Forms;

namespace Send
{
    /// <summary>
    /// Interaction logic for SendWindow.xaml
    /// </summary>
    public partial class SendWindow : Window
    {
        public SendWindow()
        {
            InitializeComponent();
            sendKeys();
        }

        private async void sendKeys()
        {
            await Task.Delay(3000);
            SendKeys.SendWait("^(a)");
        }
    }
}
