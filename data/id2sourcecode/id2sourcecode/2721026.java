    public static boolean diffFiles(File a, File b) throws IOException {
        boolean ok = true;
        ByteBuffer abuf = ByteBuffer.allocate(1024);
        ByteBuffer bbuf = ByteBuffer.allocate(1024);
        FileInputStream ais = new FileInputStream(a);
        FileInputStream bis = new FileInputStream(b);
        FileChannel aic = ais.getChannel();
        FileChannel bic = bis.getChannel();
        do {
            abuf.clear();
            aic.read(abuf);
            abuf.flip();
            bbuf.clear();
            bic.read(bbuf);
            bbuf.flip();
            if (!bbuf.equals(abuf)) {
                ok = false;
            }
        } while (ok && aic.position() != aic.size() && bic.position() != bic.size());
        aic.close();
        bic.close();
        return ok;
    }
