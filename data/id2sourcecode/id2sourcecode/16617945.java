    public JPanel getInterfaceVisualizer() {
        JPanel wrapperPanel = new JPanel(new BorderLayout());
        JPanel panel = new JPanel(new GridLayout(5, 2));
        final JLabel statusLabel = new JLabel("");
        final JLabel lastEventLabel = new JLabel("");
        final JLabel channelLabel = new JLabel("");
        final JLabel powerLabel = new JLabel("");
        final JLabel ssLabel = new JLabel("");
        final JButton updateButton = new JButton("Update");
        panel.add(new JLabel("STATE:"));
        panel.add(statusLabel);
        panel.add(new JLabel("LAST EVENT:"));
        panel.add(lastEventLabel);
        panel.add(new JLabel("CHANNEL:"));
        panel.add(channelLabel);
        panel.add(new JLabel("OUTPUT POWER:"));
        panel.add(powerLabel);
        panel.add(new JLabel("SIGNAL STRENGTH:"));
        JPanel smallPanel = new JPanel(new GridLayout(1, 2));
        smallPanel.add(ssLabel);
        smallPanel.add(updateButton);
        panel.add(smallPanel);
        updateButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                channelLabel.setText("" + getChannel());
                powerLabel.setText(getCurrentOutputPower() + " dBm (indicator=" + getCurrentOutputPowerIndicator() + "/" + getOutputPowerIndicatorMax() + ")");
                ssLabel.setText(getCurrentSignalStrength() + " dBm");
            }
        });
        Observer observer;
        this.addObserver(observer = new Observer() {

            public void update(Observable obs, Object obj) {
                if (isTransmitting()) {
                    statusLabel.setText("transmitting");
                } else if (isReceiving()) {
                    statusLabel.setText("receiving");
                } else if (radioOn) {
                    statusLabel.setText("listening for traffic");
                } else {
                    statusLabel.setText("HW off");
                }
                lastEventLabel.setText(lastEvent + " @ time=" + lastEventTime);
                channelLabel.setText("" + getChannel());
                powerLabel.setText(getCurrentOutputPower() + " dBm (indicator=" + getCurrentOutputPowerIndicator() + "/" + getOutputPowerIndicatorMax() + ")");
                ssLabel.setText(getCurrentSignalStrength() + " dBm");
            }
        });
        observer.update(null, null);
        wrapperPanel.add(BorderLayout.NORTH, panel);
        wrapperPanel.putClientProperty("intf_obs", observer);
        return wrapperPanel;
    }
