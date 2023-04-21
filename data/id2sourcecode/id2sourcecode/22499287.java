    protected void onChannelData(SshMsgChannelData msg) throws IOException {
        if (firstPacket) {
            if (dataSoFar == null) {
                dataSoFar = msg.getChannelData();
            } else {
                byte newData[] = msg.getChannelData();
                byte data[] = new byte[dataSoFar.length + newData.length];
                System.arraycopy(dataSoFar, 0, data, 0, dataSoFar.length);
                System.arraycopy(newData, 0, data, dataSoFar.length, newData.length);
                dataSoFar = data;
            }
            if (allDataCheckSubst()) {
                firstPacket = false;
                socket.getOutputStream().write(dataSoFar);
            }
        } else {
            try {
                socket.getOutputStream().write(msg.getChannelData());
            } catch (IOException ex) {
            }
        }
    }
