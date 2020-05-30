using System.Collections.Generic;
using System.ComponentModel;
using System.Threading.Tasks;
using System.Windows.Media;

namespace Send.Models
{
    class MainViewModel : INotifyPropertyChanged
    {
        #region fields

        /// <summary>
        /// The default delay is 1s, 1000ms
        /// </summary>
        private double delay = 10;
        public double delayValue
        {
            get { return delay; }
            set
            {
                delay = value;
                onChangeMany(new string[] { "delayMessage", "delayValue" });
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
                if (connected) return "CONNECTED";
                return "NOT CONNECTED";
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

        #region Functions

        /// <summary>
        /// Load data from local and prepare everything
        /// </summary>
        public async void init()
        {
            await Task.Delay(3000);
            setConnected(true);
            setMessage("From ViewModel");
            ipString = "192.168.1.1";
            delayValue = 1;
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
