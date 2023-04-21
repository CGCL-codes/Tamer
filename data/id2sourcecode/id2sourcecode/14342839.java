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
                channelLabel.setText(getChannel() + " (freq=" + getFrequency() + " MHz)");
                powerLabel.setText(getCurrentOutputPower() + " dBm (indicator=" + getCurrentOutputPowerIndicator() + "/" + getOutputPowerIndicatorMax() + ")");
                ssLabel.setText(getCurrentSignalStrength() + " dBm");
            }
