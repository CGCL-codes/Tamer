    public static Reader notZipToReader(String argument) throws InvalidPluginArgumentException {
        File file = new File(argument);
        if (file.exists()) {
            if (file.isDirectory()) {
                throw new InvalidPluginArgumentException("Illegal attempt to convert a directory " + argument + " to Reader.");
            }
            try {
                return new FileReader(file);
            } catch (FileNotFoundException ex) {
                throw new InvalidPluginArgumentException("Exception attempting to convert existing file " + argument + " to a Reader." + ex);
            }
        } else {
            try {
                URL url = new URL(argument);
                InputStream urlStream = url.openStream();
                return new InputStreamReader(urlStream);
            } catch (Exception ex) {
                return new StringReader(argument);
            }
        }
    }
