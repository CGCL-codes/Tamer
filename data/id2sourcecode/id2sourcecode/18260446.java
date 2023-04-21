    public void setRecordFile(String passedURI) throws IOException {
        if (outputStream != null) {
            outputStream.close();
            outputStream = null;
        }
        try {
            URL targetURL = Utils.getAbsoluteURL(this.recordDir, passedURI);
            if (targetURL.toURI().getScheme().equals(Utils._FILE_SCHEME_)) {
                File f = new File(targetURL.getFile());
                f.getParentFile().mkdirs();
                this.outputStream = new FileOutputStream(f);
            } else {
                URLConnection urlConnection = targetURL.openConnection();
                urlConnection.connect();
                this.outputStream = urlConnection.getOutputStream();
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
            throw new IOException(e.getMessage());
        }
    }
