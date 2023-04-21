    private Reader readFromUrl(URL url) {
        InputStreamReader inStream = null;
        try {
            URLConnection conn = url.openConnection();
            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(false);
            inStream = new InputStreamReader(conn.getInputStream());
        } catch (IOException exc) {
            _writer.println("XMLtoDOM.readFromUrl(): IOException: " + exc.getMessage());
        } catch (Exception exc) {
            _writer.println("XMLtoDOM.readFromUrl(): Exception: " + exc.getMessage());
            exc.printStackTrace(_writer);
        }
        return inStream;
    }
