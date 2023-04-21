    public String getFile(URL url) {
        int letter;
        String data = "";
        try {
            StringBuffer dataBuffer = new StringBuffer();
            InputStream in = url.openStream();
            while ((letter = in.read()) != -1) {
                dataBuffer.append((char) letter);
                System.out.print((char) letter);
            }
            data = dataBuffer.toString();
        } catch (java.io.IOException ioe) {
            System.err.println(ioe.toString());
        }
        return data;
    }
