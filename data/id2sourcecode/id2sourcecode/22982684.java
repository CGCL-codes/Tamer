    private static void extract(JarFile jarFile, JarEntry jarEntry, File destFile) throws IOException {
        long uncompressedSize = jarEntry.getSize();
        if (uncompressedSize == 0) {
            destFile.createNewFile();
        } else {
            int bufferSize = (uncompressedSize < 0) ? 8192 : Math.max(32, (int) Math.min(8192, uncompressedSize));
            BufferedXInputStream in = null;
            BufferedXOutputStream out = null;
            try {
                FileOutputStream fileOut = new FileOutputStream(destFile, false);
                FileChannel outChannel = fileOut.getChannel();
                outChannel.lock();
                out = new BufferedXOutputStream(fileOut, bufferSize);
                in = new BufferedXInputStream(jarFile.getInputStream(jarEntry), bufferSize);
                in.transferTo(out, -1);
                in.close();
                in = null;
                outChannel.truncate(outChannel.position());
                out.close();
                out = null;
            } finally {
                IO.tryClose(in);
                IO.tryClose(out);
            }
        }
    }
