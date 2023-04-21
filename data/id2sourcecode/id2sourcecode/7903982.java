    private void initPropertyChangeSupport() {
        _propChangeSupport = new PropertyChangeSupport(this);
        _serverListener = new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                _propChangeSupport.firePropertyChange("Servers", null, null);
            }
        };
        for (int j = 0; j < _favoriteChannels.getChannelCount(); j++) {
            _favoriteChannels.getChannel(j).addPropertyChangeListener(_channelListener);
        }
        _channelListener = new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                _propChangeSupport.firePropertyChange("Channels", null, null);
            }
        };
        for (int j = 0; j < _favoriteChannels.getChannelCount(); j++) {
            _favoriteChannels.getChannel(j).addPropertyChangeListener(_channelListener);
        }
        _userListener = new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                _propChangeSupport.firePropertyChange("Users", null, null);
            }
        };
        for (int k = 0; k < _favoriteUsers.getUserCount(); k++) {
            _favoriteUsers.getUser(k).addPropertyChangeListener(_userListener);
        }
    }
