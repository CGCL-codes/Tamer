    public static void writeUrlToFile(URL url, File file) {
        InputStream is;
        try {
            is = url.openStream();
        } catch (IOException e) {
            throw new RematoClientException(new ModelStatus(IStatus.ERROR, "PDF creation error. Unable to open remote URL: " + url.toExternalForm(), e));
        }
        OutputStream out;
        try {
            out = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            throw new RematoClientException(new ModelStatus(IStatus.ERROR, "PDF creation error. Unable to open local file: " + file.getAbsolutePath(), e));
        }
        byte[] buf = new byte[1024];
        int len;
        try {
            while ((len = is.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            is.close();
            out.close();
        } catch (Exception e) {
            throw new RematoClientException(new ModelStatus(IStatus.ERROR, "PDF creation error. Unable to read form remote file or write to local file: " + file.getAbsolutePath(), e));
        }
        MessageDialog.openInformation(RequirementPlugin.getInstance().getActiveShell().getShell(), "Successfully created PDF", "The file is located at: " + file.getAbsolutePath());
    }
