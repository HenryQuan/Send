using Send.Models;
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
        private MainViewModel viewModel;

        public MainWindow()
        {
            InitializeComponent();

            viewModel = new MainViewModel();
            DataContext = viewModel;

            setupUI();
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
        /// Retrieve saved data and setup UI
        /// </summary>
        private async void setupUI()
        {

        }

        #endregion
    }
}
