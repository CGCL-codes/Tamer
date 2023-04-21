    public final void Save(URL url, int seq, MidiFileInfos info) throws Exception {
        SaveAux(seq, info);
        URLConnection connection = url.openConnection();
        BufferedOutputStream tmp = new BufferedOutputStream(connection.getOutputStream(), 1000);
        seekoutput.writeTo(tmp);
        tmp.close();
        Close();
    }
