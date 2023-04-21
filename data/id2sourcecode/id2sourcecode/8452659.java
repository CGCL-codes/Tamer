    public void execute() throws SvnAntException {
        InputStream is = null;
        FileOutputStream os = null;
        try {
            os = new FileOutputStream(destFile);
            is = svnClient.getContent(url, revision);
            byte[] buffer = new byte[5000];
            int read;
            while ((read = is.read(buffer)) != -1) {
                os.write(buffer, 0, read);
            }
        } catch (Exception e) {
            throw new SvnAntException("Can't get the content of the specified file", e);
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
        }
    }
