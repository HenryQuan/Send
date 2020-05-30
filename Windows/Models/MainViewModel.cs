using System.Collections.Generic;
using System.ComponentModel;
using System.Windows.Media;

namespace Send.Models
{
    class MainViewModel : INotifyPropertyChanged
    {
        #region fields

        /// <summary>
        /// The default delay is 1s, 1000ms
        /// </summary>
        private int delay = 1000;

        /// <summary>
        /// Whether the server is connected,
        /// status string and status colour changeds depending the whether it is connected or not
        /// </summary>
        private bool connected = false;
        public bool Connected
        {
            get { return connected;  } 
            set
            {
                if (connected != value)
                {
                    connected = value;
                    // The UI is using status and brush so you need to update this
                    onChangeMany(new string[] { "status", "statusBrush" });
                }
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
