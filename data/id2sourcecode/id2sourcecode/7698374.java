    public synchronized void callBack(DataPacket dataPkt) {
        Debugger.debug(Debugger.TRACE, "LakeSink:Got some Data");
        for (int i = 0; i < dataPkt.getSize(); i++) {
            double[] data = (double[]) dataPkt.getDataAt(i);
            String chanName = dataPkt.getChannelNameAt(i);
            for (int j = 0; j < data.length; j++) {
                Date timestamp = new Date((long) (dataPkt.getTimestampAt(i, j)));
                String time = DateFormat.getDateTimeInstance().format(timestamp);
                Debugger.debug(Debugger.TRACE, "Received|" + getSink().getName() + "|" + chanName + "|" + time + "|" + "|" + j + "|" + data[j]);
            }
        }
        Debugger.debug(Debugger.TRACE, "-----------------------------------");
        updateIntoDb(dataPkt);
        sendFeedbackIfNecessary();
    }
