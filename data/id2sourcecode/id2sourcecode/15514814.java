    public static void dumpToFile(InputStream in, File f) throws FileNotFoundException, IOException {
        ReadableByteChannel ic = Channels.newChannel(in);
        FileChannel oc = null;
        try {
            oc = new FileOutputStream(f).getChannel();
            if (ic instanceof FileChannel) {
                FileChannel fic = (FileChannel) ic;
                fic.transferTo(0, fic.size(), oc);
                fic.close();
                oc.close();
            } else {
                ByteBuffer buf = ByteBuffer.allocateDirect(16 * 1024);
                while (ic.read(buf) >= 0 || buf.position() != 0) {
                    buf.flip();
                    oc.write(buf);
                    buf.compact();
                }
            }
        } finally {
            if (oc != null) oc.close();
        }
    }
