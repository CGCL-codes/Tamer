    public void generateLog(OutputStream os) throws IOException, ParseException {
        LogWriter writer = new T4CClientWriter(os, this.conf);
        LogReader reader = new T4CLogGenerator(this.logsize, this.conf);
        for (Message message = reader.read(); message != null; message = reader.read()) writer.write(message);
        writer.close();
    }
