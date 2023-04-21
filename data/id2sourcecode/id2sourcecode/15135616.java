    private void initialize(ClassLoadHelper aClassLoadHelper) throws GoException {
        InputStream f = null;
        try {
            this.classLoadHelper = aClassLoadHelper;
            String furl = null;
            File file = new File(getFileName());
            if (!file.exists()) {
                URL url = classLoadHelper.getResource(getFileName());
                if (url != null) {
                    try {
                        furl = URLDecoder.decode(url.getPath(), "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        furl = url.getPath();
                    }
                    file = new File(furl);
                    try {
                        f = url.openStream();
                    } catch (IOException ignor) {
                    }
                }
            } else {
                try {
                    f = new java.io.FileInputStream(file);
                } catch (FileNotFoundException e) {
                }
            }
            if (f == null) {
                LOG.warn("File named ' {} ' does not exist.", getFileName());
            } else {
                fileFound = true;
                filePath = (furl != null) ? furl : file.getAbsolutePath();
                fileBasename = file.getName();
            }
        } finally {
            try {
                if (f != null) {
                    f.close();
                }
            } catch (IOException ioe) {
                LOG.warn("Error closing jobs file " + getFileName(), ioe);
            }
        }
    }
