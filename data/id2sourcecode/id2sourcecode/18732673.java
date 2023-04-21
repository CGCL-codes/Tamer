    private InputStream openStream(String filename) {
        InputStream stream = null;
        try {
            URL url = new URL(filename);
            stream = url.openStream();
            return stream;
        } catch (MalformedURLException e) {
        } catch (IOException e) {
            throw new RuntimeException("Error downloading from URL " + filename);
        }
        if (!pApplet.online) {
            try {
                File file = new File(pApplet.sketchPath + File.separator + "data", filename);
                if (!file.exists()) {
                    file = new File(pApplet.sketchPath, filename);
                }
                if (file.exists()) {
                    try {
                        String path = file.getCanonicalPath();
                        String filenameActual = new File(path).getName();
                        if (filenameActual.equalsIgnoreCase(filename) && !filenameActual.equals(filename)) {
                            throw new RuntimeException("This file is named " + filenameActual + " not " + filename + ".");
                        }
                    } catch (IOException e) {
                    }
                }
                stream = new FileInputStream(file);
                if (stream != null) return stream;
            } catch (IOException ioe) {
            } catch (SecurityException se) {
            }
        }
        try {
            stream = pApplet.getClass().getResourceAsStream(filename);
            if (stream != null) return stream;
            stream = pApplet.getClass().getResourceAsStream("data/" + filename);
            if (stream != null) return stream;
            try {
                try {
                    File file = new File(pApplet.sketchPath, filename);
                    stream = new FileInputStream(file);
                    if (stream != null) return stream;
                } catch (Exception e) {
                }
                try {
                    stream = new FileInputStream(new File("data", filename));
                    if (stream != null) return stream;
                } catch (IOException e2) {
                }
                try {
                    stream = new FileInputStream(filename);
                    if (stream != null) return stream;
                } catch (IOException e1) {
                }
            } catch (SecurityException se) {
            }
            if (stream == null) {
                throw new IOException("openStream() could not open " + filename);
            }
        } catch (Exception e) {
        }
        return null;
    }
