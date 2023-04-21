    public InputStream getDataInputStream(String filename) {
        try {
            URL url = new URL(m_applet.getCodeBase(), filename);
            InputStream instream = url.openStream();
            return instream;
        } catch (IOException iox) {
            System.out.println("ERROR: " + iox.getMessage());
        }
        return null;
    }
