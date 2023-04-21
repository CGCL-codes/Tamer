    protected String getSystemIdForFileName(String fileName) {
        InputStream fileInputStream = null;
        try {
            String urlPath = null;
            File file = new File(fileName);
            if (!file.exists()) {
                URL url = getURL(fileName);
                if (url != null) {
                    urlPath = URLDecoder.decode(url.getPath());
                    try {
                        fileInputStream = url.openStream();
                    } catch (IOException ignore) {
                    }
                }
            } else {
                try {
                    fileInputStream = new FileInputStream(file);
                } catch (FileNotFoundException ignore) {
                }
            }
            if (fileInputStream == null) {
                getLog().debug("Unable to resolve '" + fileName + "' to full path, so using it as is for system id.");
                return fileName;
            } else {
                return (urlPath != null) ? urlPath : file.getAbsolutePath();
            }
        } finally {
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
            } catch (IOException ioe) {
                getLog().warn("Error closing jobs file: " + fileName, ioe);
            }
        }
    }
