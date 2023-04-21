        public void actionPerformed(ActionEvent e) {
            long stationId = stationSensorSelector.getSelectedStationId();
            long sensorId = stationSensorSelector.getSelectedSensorId();
            if (stationId != 0 && sensorId != 0) {
                ClientTransceiver transceiver = new ClientTransceiver(AppContext.instance().getChannelNumber());
                transceiver.addReceiver(AppContext.instance().getChannelNumber());
                SendMeasurementServerAction action = new SendMeasurementServerAction(new Timestamp(System.currentTimeMillis()), NumberTools.toDouble(value.getText(), 0.0), stationId, sensorId);
                action.setTransceiver(transceiver);
                ActionTools.sendToServer(action);
            }
        }
