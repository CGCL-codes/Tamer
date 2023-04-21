    public boolean write(String readerID, Vector<Byte> data) {
        TcpClient cli = clients.get(readerID);
        if (cli == null) return false;
        if (data.get(8) == MessageHeader.REQUEST_MSG) {
            RequestMessage msg = new RequestMessage(data);
            if (((RequestMessageHeader) msg.header).mode == MessageHeader.TWOWAY_MODE) {
                if (!clientThreads.containsKey(readerID)) {
                    ReplyReadThread rth = new ReplyReadThread(this, readerID);
                    clientThreads.put(readerID, rth);
                    rth.start();
                }
            }
        }
        return cli.write(data);
    }
