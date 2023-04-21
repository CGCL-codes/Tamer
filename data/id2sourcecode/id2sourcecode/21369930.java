    private String getData() {
        int letter;
        String data = "";
        try {
            StringBuffer dataBuffer = new StringBuffer();
            InputStream in = this.url.openStream();
            while ((letter = in.read()) != -1) {
                dataBuffer.append((char) letter);
            }
            data = dataBuffer.toString();
        } catch (java.io.IOException ioe) {
            System.err.println();
            System.err.println("Error in '" + this.getClass().getName() + "' " + ioe.toString());
        }
        return data;
    }
