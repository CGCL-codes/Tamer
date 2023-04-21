    public void createEps(OutputStream os) throws IOException, InterruptedException {
        final File epsFile = FileUtils.createTempFile("eps", ".eps");
        createEps(epsFile);
        int read;
        final InputStream is = new FileInputStream(epsFile);
        while ((read = is.read()) != -1) {
            os.write(read);
        }
        is.close();
        if (OptionFlags.getInstance().isKeepTmpFiles() == false) {
            epsFile.delete();
        }
    }
