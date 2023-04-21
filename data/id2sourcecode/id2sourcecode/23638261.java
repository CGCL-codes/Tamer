    private void copyFile(File dir, String path) throws IOException {
        URL entry = bundleContext.getBundle().getEntry(path);
        String file = getName(path);
        FileOutputStream outStream = new FileOutputStream(dir.getPath() + File.separator + file);
        ReadableByteChannel in = Channels.newChannel(entry.openStream());
        FileChannel out = outStream.getChannel();
        out.transferFrom(in, 0, Integer.MAX_VALUE);
        in.close();
        out.close();
    }
