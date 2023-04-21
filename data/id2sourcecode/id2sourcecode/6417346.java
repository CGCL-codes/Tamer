    public static Schranke verbinde(final InputStream quelle, final OutputStream ziel, final boolean parallel, final boolean ziel_schliessen, final int block) {
        final Schranke schranke = parallel ? new Schranke(false) : null;
        Runnable run = new Runnable() {

            public void run() {
                try {
                    BufferedInputStream bis = new BufferedInputStream(quelle);
                    BufferedOutputStream bos = new BufferedOutputStream(ziel);
                    byte[] ba = new byte[block];
                    for (int len = 0; (len = bis.read()) > -1; ) {
                        bos.write(len);
                        while ((len = bis.read(ba)) == block) bos.write(ba);
                        if (len > 0) bos.write(ba, 0, len);
                    }
                    bis.close();
                    if (ziel_schliessen) bos.close(); else bos.flush();
                    if (parallel) schranke.oeffnen();
                } catch (Throwable t) {
                    Fehler.zeig(t);
                }
            }
        };
        if (parallel) new Thread(run).start(); else run.run();
        return schranke;
    }
