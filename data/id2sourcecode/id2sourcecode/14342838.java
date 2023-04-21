            public void actionPerformed(ActionEvent e) {
                channelLabel.setText(getChannel() + " (freq=" + getFrequency() + " MHz)");
                powerLabel.setText(getCurrentOutputPower() + " dBm (indicator=" + getCurrentOutputPowerIndicator() + "/" + getOutputPowerIndicatorMax() + ")");
                ssLabel.setText(getCurrentSignalStrength() + " dBm");
            }
