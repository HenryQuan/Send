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
        public double DelayValue
        {
            get { return delay; }
            set
            {
                delay = value;
                Settings.Default.Delay_Value = value;
                Settings.Default.Save();
                onChangeMany(new string[] { "DelayMessage", "DelayValue" });

                // Update timer interval
                timer.Interval = value * 100;
            }
        }
        public string DelayMessage
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
                onChangeMany(new string[] { "Status", "StatusBrush" });
            }
        }
        public string Status
        {
            get
            {
                if (connected) return "ACTIVE";
                return "NOT ACTIVE";
            }
        }
        public Brush StatusBrush
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
        private string Address;
        public string AddressString
        {
            get { return Address; }
            set
            {
                Address = value;
                Settings.Default.IP_Address = value;
                Settings.Default.Save();
                onChange("AddressString");
            }
        }

        /// <summary>
        /// Message from the server, it need to be decoded from UTF8
        /// </summary>
        public string Message { get; private set; } = "Hello World";
        private void setMessage(string value)
        {
            if (Message != value)
            {
                Message = value;
                onChange("Message");
            }
        }

        /// <summary>
        /// This is the error message from web client
        /// </summary>
        public string ErrorMessage { get; private set; } = "";
        private void setErrorMessage(string value)
        {
            if (ErrorMessage != value)
            {
                ErrorMessage = value;
                onChange("ErrorMessage");
            }
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
            Address = Settings.Default.IP_Address;
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
            updateListener(validateIPAddress());
        }

        private void listen(object sender, ElapsedEventArgs e)
        {
            try
            {
                using (var client = new WebClient())
                {
                    // Get the message from the ip
                    string msg = client.DownloadString("http://" + Address);
                    msg = msg.Replace("\\x a", "");
                    // Clear error message if the request was successful
                    setErrorMessage("");
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
                Console.WriteLine(Address);
                setErrorMessage(ex.Message);
                updateListener(false);
            }
        }

        #endregion

        #region Utils

        /// <summary>
        /// Update listener state
        /// </summary>
        /// <param name="value"></param>
        private void updateListener(bool value)
        {
            setConnected(value);
            timer.Enabled = value;
        }

        /// <summary>
        /// Check whether the IP Address is valid
        /// </summary>
        /// <returns></returns>
        private bool validateIPAddress()
        {
            // Check if it is null
            if (string.IsNullOrEmpty(Address)) return false;
            // IPv4 has . and iPv6 has :
            if (Address.Contains(".") || Address.Contains(":")) return true;
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
