using Send.Models;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Forms;
using System.Windows.Input;

namespace Send
{
    /// <summary>
    /// Interaction logic for MainWindow.xaml
    /// </summary>
    public partial class MainWindow : Window
    {
        private MainViewModel viewModel;

        public MainWindow()
        {
            InitializeComponent();

            // Setup view model
            viewModel = new MainViewModel();
            DataContext = viewModel;
        }

        private async void sendKeys()
        {
            await Task.Delay(3000);
            SendKeys.SendWait("^(a)");
        }

        /// <summary>
        /// Ask view model to connect to the server
        /// </summary>
        private void startListener()
        {
            Keyboard.ClearFocus();
            viewModel.startListener();
        }

        #region UI events

        private void ipBox_KeyDown(object sender, System.Windows.Input.KeyEventArgs e)
        {
            if (e.Key == Key.Return)
            {
                startListener();
            }
        }

        private void connectButton_Click(object sender, RoutedEventArgs e)
        {
            startListener();
        }

        private void openBrowserItem_Click(object sender, RoutedEventArgs e)
        {
            viewModel.openBrowser();
        }

        #endregion
    }
}
