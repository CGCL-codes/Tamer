    private File copyFile(File dir, String path) throws IOException {
        URL entry = bContext.getBundle().getEntry(path);
        path = path.replace('/', File.separatorChar);
        File outFile = File.createTempFile(getName(path) + "-", ".tmp", dir);
        FileOutputStream outStream = new FileOutputStream(outFile);
        ReadableByteChannel in = Channels.newChannel(entry.openStream());
        FileChannel out = outStream.getChannel();
        out.transferFrom(in, 0, Integer.MAX_VALUE);
        in.close();
        out.close();
        return outFile;
    }
