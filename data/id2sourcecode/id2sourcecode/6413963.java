    public boolean flushData() throws LockFailureException, IOException {
        List<String> filesToBackup = FileUtils.listRecursively(extractDirectory, CWD_FILE_FILTER);
        File destZip = getTargetZipFile();
        RobustFileOutputStream rOut = new RobustFileOutputStream(destZip);
        OutputStream out = rOut;
        if (isPdbk(destZip)) out = new XorOutputStream(out, PDBK_XOR_BITS);
        try {
            ZipOutputStream zipOut = new ZipOutputStream(new BufferedOutputStream(out));
            for (String filename : filesToBackup) {
                File f = new File(extractDirectory, filename);
                ZipEntry e = new ZipEntry(filename);
                e.setTime(f.lastModified());
                zipOut.putNextEntry(e);
                FileUtils.copyFile(f, zipOut);
                zipOut.closeEntry();
            }
            zipOut.finish();
            zipOut.flush();
        } catch (IOException ioe) {
            rOut.abort();
            throw ioe;
        }
        out.close();
        return true;
    }
