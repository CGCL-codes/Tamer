    private InputStream openStream(String dataName) {
        InputStream inStream = null;
        try {
            URL url = new URL(dataName);
            inStream = url.openStream();
        } catch (Exception exc) {
            File file = new File(dataName);
            if (file.exists() && file.canRead()) {
                try {
                    inStream = new FileInputStream(file);
                } catch (FileNotFoundException exc1) {
                }
            }
        }
        return inStream;
    }
