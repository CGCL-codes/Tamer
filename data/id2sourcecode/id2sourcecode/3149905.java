    public void execute() {
        try {
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(dstFile));
            out.setLevel(9);
            for (File f : new File(srcDir).listFiles()) {
                out.putNextEntry(new ZipEntry(f.getName()));
                FileUtils.copy(f, out);
                out.closeEntry();
            }
            out.close();
        } catch (IOException ioe) {
            throw new BuildException(ioe);
        }
    }
