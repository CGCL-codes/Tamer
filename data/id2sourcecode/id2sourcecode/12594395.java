    protected OutputStream getCompressedOutputStream(File f) throws IOException {
        OutputStream os = new BufferedOutputStream(new FileOutputStream(f));
        if (getCompressionFormat() == ZIP) {
            ZipOutputStream ret = new ZipOutputStream(os);
            ret.putNextEntry(new ZipEntry(logName));
            return ret;
        } else {
            return new GZIPOutputStream(os);
        }
    }
