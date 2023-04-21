    public synchronized void serialSend(int function, int param1, int param2) {
        LOG.info("serialSend fn " + function + " p1 " + param1 + " p2 " + param2);
        byte data[] = new byte[3];
        data[0] = (byte) function;
        data[1] = (byte) param1;
        data[2] = (byte) param2;
        if (connectedThread != null) {
            connectedThread.write(data);
        } else {
            LOG.error("currently not connected");
        }
    }
