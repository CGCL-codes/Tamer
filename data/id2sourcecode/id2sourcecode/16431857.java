    protected void updateView() {
        String channelName = "";
        if (channelModel != null) {
            Channel channel = channelModel.getChannel();
            if (channel != null) {
                channelName = channelModel.getChannelName();
                try {
                    if (channel.isConnected()) {
                        String units = channel.getUnits();
                        _unitsField.setText(units);
                    }
                } catch (ConnectionException exception) {
                } catch (GetException exception) {
                    System.err.println(exception);
                }
            }
            _enableButton.setEnabled(channelModel.getChannel() != null);
            _enableButton.setSelected(channelModel.isEnabled());
            scaleControl.setEnabled(channelModel.isEnabled());
            scaleControl.setValue(1.0 / channelModel.getSignalScale());
            signalOffsetTractor.setEnabled(channelModel.isEnabled());
            double signalOffset = channelModel.getSignalOffset();
            signalOffsetTractor.setValue((long) (signalOffset / signalOffsetResolution));
            signalOffsetValueField.setText(offsetFormat.format(signalOffset));
        } else {
            _enableButton.setEnabled(false);
        }
        channelField.setText(channelName);
        repaint();
    }
