    private void w3(InputStream fis, OutputStream os, long sleep, String pfx) throws Exception {
        byte[] bb = new byte[1024];
        int bread = -1;
        while ((bread = fis.read(bb)) != -1) {
            os.write(bb, 0, bread);
            os.flush();
            if (sleep > 0) {
                try {
                    Thread.sleep(sleep);
                } catch (Exception exc) {
                }
            }
            if (pfx != null) diagos.println(pfx + bread);
        }
    }
