using System.ComponentModel;
using System.Windows.Media;

namespace Send.Models
{
    class MainViewModel : INotifyPropertyChanged
    {
        /// <summary>
        /// The default delay is 1s, 1000ms
        /// </summary>
        private int delay = 1000;

        /// <summary>
        /// This checks whether the request is active
        /// </summary>
        private bool connected = false;

        /// <summary>
        /// This describes the current status 
        /// </summary>
        public string status
        {
            get
            {
                if (connected) return "CONNeCTED";
                return "NOT CONNECTED";
            }
        }

        /// <summary>
        /// This is the best brush colour for current status
        /// </summary>
        public Brush statusBrush
        {
            get
            {
                if (connected) return Brushes.DarkGreen;
                return Brushes.Red;
            }
        }


        #region INotifyPropertyChanged related
        public event PropertyChangedEventHandler PropertyChanged;

        protected void RaisePropertyChangedEvent(string propertyName)
        {
            PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(propertyName));
        }
        #endregion
    }
}
