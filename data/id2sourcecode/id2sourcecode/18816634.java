    void goAhead(Request _req, OutputStream _os, int _con_to) {
        this.req = _req;
        this.os = _os;
        this.con_to = _con_to;
        if (os == null) bos = new ByteArrayOutputStream();
        Log.write(Log.CONN, "OutS:  Stream ready for writing");
        if (bos != null) Log.write(Log.CONN, "OutS:  Buffering all data before sending " + "request");
    }
