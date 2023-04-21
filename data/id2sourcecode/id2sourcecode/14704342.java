    public AmandaFile(URL url) throws BadFormException, IOException, VisADException {
        InputStream is = url.openStream();
        try {
            loadFile(new BufferedReader(new InputStreamReader(is)));
        } finally {
            try {
                is.close();
            } catch (IOException ioe) {
            }
        }
    }
