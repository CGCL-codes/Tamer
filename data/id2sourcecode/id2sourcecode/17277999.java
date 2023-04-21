    public synchronized byte[] getFile(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        int size = conn.getContentLength();
        InputStream is = conn.getInputStream();
        pos = 0;
        leido = 0;
        int ini = leido;
        try {
            do {
                synchronized (bwm) {
                    while (bwm.leche < bsize) {
                        try {
                            bwm.wait();
                        } catch (InterruptedException e) {
                        }
                    }
                    bwm.leche -= bsize;
                }
                res = is.read(buffer, pos, bsize);
                if (res > -1) {
                    pos += res;
                    descarga += res;
                }
                mostrar("Descargando (" + (pos - ini) / 1024 + " de " + size / 1024 + " KB) | " + (pos - ini) * 100 / size + "% | [ " + (double) ((int) ((double) bwm.promedio / 1024 * 100)) / 100 + " KB/s ]");
            } while (res > -1);
        } catch (SocketTimeoutException e) {
            skt.close();
            throw e;
        }
        is.close();
        byte bin[] = new byte[pos - ini];
        for (int x = ini; x < pos; x++) {
            bin[x - ini] = buffer[x];
        }
        return bin;
    }
