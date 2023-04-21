    public MeasuredValue(String chanName) {
        if (chanName != null && badChannelNames.containsKey(chanName) == false) {
            callBack = new IEventSinkValue() {

                public void eventValue(ChannelRecord record, Channel chan) {
                    currValueMonitor = record.doubleValue();
                }
            };
            ch_ = ChannelFactory.defaultFactory().getChannel(chanName);
            try {
                ch_.addMonitorValue(callBack, Monitor.VALUE);
            } catch (ConnectionException e) {
                badChannelNames.put(chanName, null);
                ch_ = null;
            } catch (MonitorException e) {
                badChannelNames.put(chanName, null);
                ch_ = null;
            }
        }
        currValueTextField.setBackground(Color.white);
        meanValueTextField.setBackground(Color.white);
        currValueTextField.setEditable(false);
        meanValueTextField.setEditable(false);
        setFontForAll(new Font(meanValueTextField.getFont().getFamily(), Font.BOLD, 10));
        currValueTextField.setHorizontalAlignment(JTextField.CENTER);
        meanValueTextField.setHorizontalAlignment(JTextField.CENTER);
        currValueTextField.setText(null);
        meanValueTextField.setText(null);
    }
