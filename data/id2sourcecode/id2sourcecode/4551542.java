    public static InputStream getInputStream(File basedir, String pathOrUrl) {
        InputStream is = null;
        if (UURI.hasScheme(pathOrUrl)) {
            try {
                URL url = new URL(pathOrUrl);
                is = url.openStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            File source = new File(pathOrUrl);
            if (!source.isAbsolute() && basedir != null) {
                source = new File(basedir, pathOrUrl);
            }
            try {
                is = new FileInputStream(source);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return is;
    }
