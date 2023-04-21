    public void callBack(DataPacket dataPkt) {
        for (int i = 0; i < dataPkt.getSize(); i++) {
            Object dataTmp = dataPkt.getDataAt(i);
            if (!(dataTmp instanceof String[])) {
                throw new IllegalStateException("Feedback message is expected in " + "String format");
            }
            String[] data = (String[]) dataTmp;
            String chanName = dataPkt.getChannelNameAt(i);
            for (int j = 0; j < data.length; j++) {
                Date time = new Date((long) (dataPkt.getTimestampAt(i, j)));
                receiveFeedback(data[j], time);
            }
        }
    }
