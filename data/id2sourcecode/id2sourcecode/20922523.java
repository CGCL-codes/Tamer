        }
        return in;
    }

    public DataInputStream openDataIn(String filename) {
        DataInputStream data_in = null;
        InputStream in = openTextIn(filename);
        if (in != null) {
            data_in = new DataInputStream(in);
        }
        return data_in;
    }

    public StreamTokenizer openTokenIn(String filename) {
        StreamTokenizer tok_in = null;
        InputStream in = openTextIn(filename);
        if (in != null) {
            Reader r = new BufferedReader(new InputStreamReader(in));
            tok_in = new StreamTokenizer(r);
        }
