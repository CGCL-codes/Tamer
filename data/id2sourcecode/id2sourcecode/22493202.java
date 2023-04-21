    public void handle(Object aTask) {
        DataHolder dataReceiver = (DataHolder) aTask;
        try {
            ByteArrayOutputStream stream = getStream(dataReceiver);
            parse(stream.toByteArray(), dataReceiver.getChannel());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
