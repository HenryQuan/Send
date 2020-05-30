using System.Threading.Tasks;
using System.Windows;
using System.Windows.Forms;

namespace Send
{
    /// <summary>
    /// Interaction logic for MainWindow.xaml
    /// </summary>
    public partial class MainWindow : Window
    {
        public MainWindow()
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
