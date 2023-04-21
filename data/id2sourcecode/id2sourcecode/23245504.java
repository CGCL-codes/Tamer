    public static File getResourceAsFile(String resourcePath) throws IOException {
        InputStream is = null;
        FileOutputStream fos = null;
        ClassLoader loader = ClassLoader.getSystemClassLoader();
        URL url = loader.getResource(resourcePath);
        if (url == null) {
            url = ResourceUtilities.class.getClassLoader().getResource(resourcePath);
        }
        if (url == null) {
            return null;
        }
        try {
            File file = new File(resourcePath);
            String filename = file.getName();
            String suffix = null;
            String prefix = null;
            int idx = filename.lastIndexOf('.');
            if (idx > -1) {
                suffix = ".tmp" + filename.substring(idx);
                prefix = "temp." + filename.substring(0, idx);
            } else {
                suffix = null;
                prefix = "temp." + filename;
            }
            File tmpFile = File.createTempFile(prefix, suffix);
            tmpFile.deleteOnExit();
            is = url.openStream();
            fos = new FileOutputStream(tmpFile);
            copy(is, fos);
            fos.flush();
            fos.close();
            is.close();
            return tmpFile;
        } finally {
            if (fos != null) {
                fos.flush();
                fos.close();
            }
            if (is != null) {
                is.close();
            }
        }
    }
