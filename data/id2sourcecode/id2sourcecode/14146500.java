    @Override
    public void run() {
        URLConnection urlConnection;
        Vector<Byte> buildit = new Vector<Byte>();
        try {
            urlConnection = url.openConnection();
            BufferedInputStream in = new BufferedInputStream(urlConnection.getInputStream());
            byte temp = 0;
            while ((temp = (byte) in.read()) != -1) {
                buildit.add(temp);
            }
        } catch (IOException e) {
            callback.failed(e, url.toExternalForm(), uniqueCode);
        }
        data = new byte[buildit.size()];
        for (int i = 0; i < data.length; i++) {
            data[i] = buildit.elementAt(i);
        }
        String serverMessage = new String(data);
        callback.dataReturned(data, url.toExternalForm(), uniqueCode);
    }
