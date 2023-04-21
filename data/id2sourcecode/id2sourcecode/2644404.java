    public void callBack(DataPacket dataPkt) {
        System.out.println("Stub:Got some Data");
        String source = null;
        for (int i = 0; i < dataPkt.getSize(); i++) {
            double[] data = (double[]) dataPkt.getDataAt(i);
            String chanName = dataPkt.getChannelNameAt(i);
            for (int j = 0; j < data.length; j++) {
                String time = DateFormat.getDateTimeInstance().format(new Date((long) (dataPkt.getTimestampAt(i, j))));
                System.out.println("Received|" + sink.getName() + "|" + chanName + "|" + time + "|" + "|" + j + "|" + data[j]);
                if (source == null) {
                    int index = chanName.indexOf('/');
                    if (index != -1) {
                        source = chanName.substring(0, index);
                    }
                }
            }
        }
        if (feedbackReqd) {
            sink.sendFeedback(source, "ghijkl");
        }
        System.out.println("-----------------------------------");
    }
