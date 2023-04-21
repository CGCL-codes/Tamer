    public boolean checkOK() throws IOException {
        WirelessRope.mConsole.writeLog("LogSender.checkOK: open " + mUrl);
        StreamConnection streamCon = (StreamConnection) javax.microedition.io.Connector.open(mUrl, Connector.READ_WRITE, true);
        OutputStream out = streamCon.openOutputStream();
        WirelessRope.mConsole.writeLog("LogSender.checkOK: write command");
        out.write("transferstatus\n".getBytes());
        out.flush();
        out.close();
        WirelessRope.mConsole.writeLog("LogSender.checkOK: read status");
        DataInputStream in = streamCon.openDataInputStream();
        byte[] buf = new byte[1];
        int len = in.read(buf);
        WirelessRope.mConsole.writeLog("LogSender.checkOK: read " + len + " bytes");
        String status = "";
        if (len > 0) {
            status = new String(buf, 0, len);
        } else {
            WirelessRope.mConsole.writeLog("LogSender.checkOK: throw IOEx");
            throw new IOException();
        }
        in.close();
        WirelessRope.mConsole.writeLog("LogSender.checkOK: close");
        streamCon.close();
        if (status.compareTo("A") == 0) {
            WirelessRope.mConsole.writeLog("LogSender.checkOK: OK");
            return true;
        } else {
            WirelessRope.mConsole.writeLog("LogSender.checkOK: NOT OK");
            return false;
        }
    }
