using Send.Properties;
using System;
using System.ComponentModel;
using System.Net;
using System.Text;
using System.Text.RegularExpressions;
using System.Timers;
using System.Windows.Media;

namespace Send.Models
{
    class MainViewModel : INotifyPropertyChanged
    {
        #region Fields with UI binding

        /// <summary>
        /// The default delay is 1s, 1000ms (remeber to x100 to make it ms)
        /// </summary>
        private double delay;
        public double delayValue
        {
            get { return delay; }
            set
            {
                delay = value;
                Settings.Default.Delay_Value = value;
                Settings.Default.Save();
                onChangeMany(new string[] { "delayMessage", "delayValue" });

                // Update timer interval
                timer.Interval = value * 100;
            }
        }
        public string delayMessage
        {
            get
            {
                // Only one digit
                return $"Delay: {delay * 100:#}ms";
            }
        }

        /// <summary>
        /// Whether the server is connected,
        /// status string and status colour changeds depending the whether it is connected or not
        /// </summary>
        private bool connected = false;
        private void setConnected(bool value)
        {
            if (connected != value)
            {
                connected = value;
                // The UI is using status and brush so you need to update this
                onChangeMany(new string[] { "status", "statusBrush" });
            }
        }
        public string status
        {
            get
            {
                if (connected) return "ACTIVE";
                return "NOT ACTIVE";
            }
        }
        public Brush statusBrush
        {
            get
            {
                if (connected) return Brushes.DarkGreen;
                return Brushes.Red;
            }
        }

        /// <summary>
        /// This is the current ip address we are using
        /// </summary>
        private string ipAddress;
        public string ipString
        {
            get { return ipAddress; }
            set
            {
                ipAddress = value;
                Settings.Default.IP_Address = value;
                Settings.Default.Save();
                onChange("ipString");
            }
        }

        /// <summary>
        /// Current message from the mobile
        /// </summary>
        private string message = "Hello World";
        private void setMessage(string value)
        {
            if (message != value)
            {
                message = value;
                // The UI is using status and brush so you need to update this
                onChange("message");
            }
        }
        public string Message
        {
            get { return message; }
        }

        #endregion

        /// <summary>
        /// This handles http rquest
        /// </summary>
        private Timer timer = new Timer();

        /// <summary>
        /// This save the previous message to prevent duplications over time
        /// </summary>
        private string previousMessage = "";

        #region Functions

        public MainViewModel()
        {
            // Load data from Settings
            ipAddress = Settings.Default.IP_Address;
            delay = Settings.Default.Delay_Value;

            // Setup timer
            timer.Elapsed += new ElapsedEventHandler(listen);
            // Update timer interval
            timer.Interval = delay * 100;
            // Start listener if IP address is valid
            startListener();
        }

        public void startListener()
        {
            setConnected(validateIPAddress());
            timer.Enabled = connected;
        }

        private void listen(object sender, ElapsedEventArgs e)
        {
            try
            {
                using (var client = new WebClient())
                {
                    // Get the message from the ip
                    string msg = client.DownloadString("http://" + ipAddress);
                    msg = msg.Replace("\\x a", "");
                    // From https://stackoverflow.com/questions/22468026/how-should-i-decode-a-utf-8-string
                    string readableMsg = Encoding.UTF8.GetString(Array.ConvertAll(Regex.Unescape(msg).ToCharArray(), c => (byte)c));
                    if (!string.IsNullOrEmpty(readableMsg) && readableMsg != previousMessage)
                    {
                        previousMessage = readableMsg;
                        setMessage(readableMsg);
                    }
                }
            }
            catch (Exception ex)
            {
                Console.WriteLine(ipAddress);
                Console.WriteLine(ex.Message);
            }
        }

        #endregion

        #region Utils

        /// <summary>
        /// Check whether the IP Address is valid
        /// </summary>
        /// <returns></returns>
        private bool validateIPAddress()
        {
            // Check if it is null
            if (string.IsNullOrEmpty(ipAddress)) return false;
            // IPv4 has . and iPv6 has :
            if (ipAddress.Contains(".") || ipAddress.Contains(":")) return true;
            return false;
        }

        #endregion

        #region INotifyPropertyChanged related
        public event PropertyChangedEventHandler PropertyChanged;

        private void onChange(string propertyName)
        {
            PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(propertyName));
        }

        private void onChangeMany(string[] propertyNames)
        {
            foreach (string name in propertyNames)
            {
                PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(name));
            }
        }
        #endregion
    }
}
