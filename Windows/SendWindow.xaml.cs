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

            SendKeys.SendWait("{^c}");
        }
    }
}
