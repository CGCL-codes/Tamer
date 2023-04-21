    public void configureSensor(WindRoseInstrumentSensor sensor, WindRoseInstrument windRoseInstrument) {
        if (historyRequestAlreadySend.contains(sensor)) {
            return;
        }
        incSensorCount();
        historyRequestAlreadySend.add(sensor);
        double channelNumber = AppContext.instance().getChannelNumber();
        ClientTransceiver clientTransceiver = new ClientTransceiver(channelNumber);
        clientTransceiver.addReceiver(channelNumber);
        if (windRoseInstrument.getMinExtrema() == 1) {
            Calendar calendar = new GregorianCalendar();
            calendar.add(Calendar.MINUTE, -10);
            Date startDate = calendar.getTime();
            Date stopDate = new Date();
            windRoseInstrument.getSensorConfig().setStartDate(startDate);
            windRoseInstrument.getSensorConfig().setStopDate(stopDate);
            final HistoricalDataRequestAction historyDataServerAction = new HistoricalDataRequestAction(windRoseInstrument.getUniqueId(), sensor.getStationId(), sensor.getSensorId(), sensor.getStartDate(), sensor.getStopDate());
            historyDataServerAction.setTransceiver(clientTransceiver);
            final ActionProcessorRegistry actionProcessorRegistry = Engine.instance().getActionProcessorRegistry();
            CommandTools.performAsync(new Command("historyrequest") {

                public boolean canPerform() {
                    return true;
                }

                public void perform() {
                    ActionTools.sendToServer(historyDataServerAction);
                }
            });
        }
        if (windRoseInstrument.getHourExtrema() == 1) {
            Calendar calendar = new GregorianCalendar();
            calendar.add(Calendar.HOUR_OF_DAY, -1);
            Date startDate = calendar.getTime();
            Date stopDate = new Date();
            windRoseInstrument.getSensorConfig().setStartDate(startDate);
            windRoseInstrument.getSensorConfig().setStopDate(stopDate);
            final HistoricalDataRequestAction historyDataServerAction = new HistoricalDataRequestAction(windRoseInstrument.getUniqueId(), sensor.getStationId(), sensor.getSensorId(), sensor.getStartDate(), sensor.getStopDate());
            historyDataServerAction.setTransceiver(clientTransceiver);
            final ActionProcessorRegistry actionProcessorRegistry = Engine.instance().getActionProcessorRegistry();
            CommandTools.performAsync(new Command("historyrequest") {

                public boolean canPerform() {
                    return true;
                }

                public void perform() {
                    ActionTools.sendToServer(historyDataServerAction);
                }
            });
        }
    }
