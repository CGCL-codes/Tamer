    public RadioConnection createConnections(Radio sender) {
        RadioConnection newConnection = new RadioConnection(sender);
        if (SUCCESS_RATIO_TX < 1.0 && random.nextDouble() > SUCCESS_RATIO_TX) {
            return newConnection;
        }
        double moteTransmissionRange = TRANSMITTING_RANGE * ((double) sender.getCurrentOutputPowerIndicator() / (double) sender.getOutputPowerIndicatorMax());
        double moteInterferenceRange = INTERFERENCE_RANGE * ((double) sender.getCurrentOutputPowerIndicator() / (double) sender.getOutputPowerIndicatorMax());
        DestinationRadio[] potentialDestinations = dgrm.getPotentialDestinations(sender);
        if (potentialDestinations == null) {
            return newConnection;
        }
        Position senderPos = sender.getPosition();
        for (DestinationRadio dest : potentialDestinations) {
            Radio recv = dest.radio;
            Position recvPos = recv.getPosition();
            if (sender.getChannel() >= 0 && recv.getChannel() >= 0 && sender.getChannel() != recv.getChannel()) {
                continue;
            }
            double distance = senderPos.getDistanceTo(recvPos);
            if (distance <= moteTransmissionRange) {
                if (!recv.isReceiverOn()) {
                    newConnection.addInterfered(recv);
                    recv.interfereAnyReception();
                } else if (recv.isInterfered()) {
                    newConnection.addInterfered(recv);
                } else if (recv.isTransmitting()) {
                    newConnection.addInterfered(recv);
                } else if (recv.isReceiving() || (SUCCESS_RATIO_RX < 1.0 && random.nextDouble() > SUCCESS_RATIO_RX)) {
                    newConnection.addInterfered(recv);
                    recv.interfereAnyReception();
                    for (RadioConnection conn : getActiveConnections()) {
                        if (conn.isDestination(recv)) {
                            conn.addInterfered(recv);
                        }
                    }
                } else {
                    newConnection.addDestination(recv);
                }
            } else if (distance <= moteInterferenceRange) {
                newConnection.addInterfered(recv);
                recv.interfereAnyReception();
            }
        }
        return newConnection;
    }
