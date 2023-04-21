    public MRMRadioConnection createConnections(Radio sendingRadio) {
        Position sendingPosition = sendingRadio.getPosition();
        MRMRadioConnection newConnection = new MRMRadioConnection(sendingRadio);
        for (Radio listeningRadio : getRegisteredRadios()) {
            if (sendingRadio == listeningRadio) {
                continue;
            }
            if (sendingRadio.getChannel() >= 0 && listeningRadio.getChannel() >= 0 && sendingRadio.getChannel() != listeningRadio.getChannel()) {
                continue;
            }
            double listeningPositionX = listeningRadio.getPosition().getXCoordinate();
            double listeningPositionY = listeningRadio.getPosition().getYCoordinate();
            double[] probData = currentChannelModel.getProbability(sendingPosition.getXCoordinate(), sendingPosition.getYCoordinate(), listeningPositionX, listeningPositionY, -Double.MAX_VALUE);
            if (random.nextFloat() < probData[0]) {
                if (listeningRadio.isInterfered()) {
                    newConnection.addInterfered(listeningRadio, probData[1]);
                } else if (listeningRadio.isReceiving()) {
                    newConnection.addInterfered(listeningRadio, probData[1]);
                    listeningRadio.interfereAnyReception();
                    MRMRadioConnection existingConn = null;
                    for (RadioConnection conn : getActiveConnections()) {
                        for (Radio dstRadio : ((MRMRadioConnection) conn).getDestinations()) {
                            if (dstRadio == listeningRadio) {
                                existingConn = (MRMRadioConnection) conn;
                                break;
                            }
                        }
                    }
                    if (existingConn != null) {
                        existingConn.addInterfered(listeningRadio);
                        listeningRadio.interfereAnyReception();
                    }
                } else {
                    newConnection.addDestination(listeningRadio, probData[1]);
                }
            } else if (probData[1] > currentChannelModel.getParameterDoubleValue("bg_noise_mean")) {
                newConnection.addInterfered(listeningRadio, probData[1]);
                listeningRadio.interfereAnyReception();
            }
        }
        return newConnection;
    }
