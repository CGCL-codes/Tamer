    public void run() {
        setState(Threadable.CLOSING);
        for (Iterator i = sensors.iterator(); i.hasNext(); ) {
            Long sensorId = (Long) i.next();
            boolean firstIteration = true;
            long tmpStartDate = 0;
            long tmpStopDate = 0;
            long yearIterations = stopYear - startYear;
            if (yearIterations == 0) {
                yearIterations = 1;
            }
            double oldMin = 0;
            double oldMid = 0;
            double oldMax = 0;
            double oldTime = 0;
            for (int y = 0; y < yearIterations; ++y) {
                if (startYear == 0) {
                    tmpStartDate = startDate;
                    tmpStopDate = stopDate;
                } else {
                    tmpStartDate = startDate - ((yearIterations - y) * 1000 * 60 * 60 * 24 * 365);
                    tmpStopDate = stopDate - ((yearIterations - 1 - y) * 1000 * 60 * 60 * 24 * 365);
                }
                Properties props = new Properties();
                Object[] params = new Object[3];
                params[0] = sensorId;
                params[1] = new Timestamp(tmpStartDate);
                params[2] = new Timestamp(tmpStopDate);
                String select;
                select = "select avg(value) as valueAvg, max(at) as atMax, max(value) as valueMax, min(value) as valueMin" + " from Measurement where sensorid=? and at>=? and at<?";
                props = new Properties();
                props.setProperty("select", select);
                props.put("handler", this);
                props.put("params", params);
                CommandTools.performSimple("persist.Select", props);
                if (firstIteration) {
                    firstIteration = false;
                    oldMin = currentMin;
                    oldMid = currentMid;
                    oldMax = currentMax;
                    oldTime = currentTime;
                }
                if (currentMin < oldMin) {
                    oldMin = currentMin;
                }
                if (currentMax > oldMax) {
                    oldMax = currentMax;
                }
                oldMin = (oldMin + currentMin) / 2;
                oldTime = (oldTime + currentTime) / 2;
                try {
                    Thread.sleep(1);
                } catch (Exception x) {
                }
            }
            output.add(new Double(sensorId.doubleValue()));
            output.add(new Double(oldMin));
            output.add(new Double(oldMid));
            output.add(new Double(oldMax));
        }
        ClientTransceiver transiever = new ClientTransceiver(receiver);
        transiever.addReceiver(receiver);
        MinMidMaxQueryResponse mMMQR = new MinMidMaxQueryResponse(instrumentUniqueId, output, startDate, stopDate);
        mMMQR.setTransceiver(transiever);
        ActionTools.sendToClient(mMMQR);
    }
