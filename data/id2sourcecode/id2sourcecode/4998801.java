    public static final void readHttpURL(File output, HttpURLConnection url) throws IOException {
        url.setAllowUserInteraction(true);
        url.connect();
        DataInputStream in = new DataInputStream(url.getInputStream());
        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(output));
        try {
            if (url.getResponseCode() != HttpURLConnection.HTTP_OK) {
                while (true) {
                    out.write((char) in.readUnsignedByte());
                }
            }
        } catch (EOFException ex) {
        } catch (Exception ex) {
            Log.exception(ex);
        } finally {
            out.close();
        }
    }
