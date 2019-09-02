using System;
using System.Net;
using System.Text;
using System.Text.RegularExpressions;
using System.Timers;
using System.Windows;
using System.Diagnostics;

namespace SendText
{
    /// <summary>
    /// Interaction logic for MainWindow.xaml
    /// </summary>
    public partial class MainWindow : Window
    {
        public string ipAddress { set; get; }
        private Timer timer;
        private string prev = "";

        public MainWindow()
        {
            InitializeComponent();
            timer = new Timer();
            timer.Elapsed += new ElapsedEventHandler(getTextFromPhone);
            timer.Interval = 3000;
            timer.Enabled = false;
            // Put IP address back
            ipBox.Text = Properties.Settings.Default.ip_address;
        }

        private void getTextFromPhone(object sender, ElapsedEventArgs e)
        {
            try
            {
                using (WebClient client = new WebClient())
                {
                    string msg = client.DownloadString("http://" + ipAddress);
                    msg = msg.Replace("\\x a", "");
                    // Run on UI thread safely
                    phoneBox.Dispatcher.Invoke(new Action(() => {
                        // From https://stackoverflow.com/questions/22468026/how-should-i-decode-a-utf-8-string
                        string phone = Encoding.UTF8.GetString(Array.ConvertAll(Regex.Unescape(msg).ToCharArray(), c => (byte)c));
                        phoneBox.Text = phone;
                        // Only copy when text changed
                        if (!string.IsNullOrEmpty(phone) && phone != prev)
                        {
                            prev = phone;
                            Clipboard.SetText(phone);
                        }
                    }));
                }
                Properties.Settings.Default.ip_address = ipAddress;
                Properties.Settings.Default.Save();
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex.Message);
            }
        }

        private void receiveText(object sender, RoutedEventArgs e)
        {
            ipAddress = ipBox.Text;
            if (!string.IsNullOrEmpty(ipAddress))
            {
                timer.Enabled = true;
            }
            else
            {
                timer.Enabled = false;
            }
        }

        private void GitHub_Click(object sender, RoutedEventArgs e)
        {
            Process.Start("https://github.com/HenryQuan/SendText/releases/latest");
        }

        private void About_Click(object sender, RoutedEventArgs e)
        {
            MessageBox.Show("SendText receives messages from your Android device\nCheck out more here - https://github.com/HenryQuan/SendText/");
        }
    }
}
