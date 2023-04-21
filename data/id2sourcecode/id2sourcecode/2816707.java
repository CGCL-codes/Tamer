    public void execute(SvnExecutor svn) throws Exception {
        File result = TempFileFactory.get().createTempFile("svnLocDiffFile", ".tmp");
        InputStream in = svn.exec("cat", catArgs);
        OutputStream out = new FileOutputStream(result);
        FileUtils.copyFile(in, out);
        in.close();
        out.close();
        this.tmpFile = result;
    }
