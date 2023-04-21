    @Override
    protected Void doInBackground() throws Exception {
        byte[] buffer = new byte[64 * 1024];
        final long t0 = System.currentTimeMillis();
        try {
            URL u = new URL(url);
            URLConnection uc = u.openConnection();
            final int bt = uc.getContentLength();
            int readSoFar = 0;
            InputStream in = u.openStream();
            try {
                FileOutputStream fout = new FileOutputStream(localFile);
                try {
                    do {
                        final int read = in.read(buffer);
                        if (read > 0) {
                            sha1.update(buffer, 0, read);
                            fout.write(buffer, 0, read);
                            readSoFar += read;
                            final int freadsofar = readSoFar;
                            SwingUtilities.invokeLater(new Runnable() {

                                @Override
                                public void run() {
                                    DownloadProgress pg = new DownloadProgress();
                                    pg.bytesReceived = freadsofar;
                                    pg.startTimestamp = t0;
                                    pg.bytesTotal = bt;
                                    callback.progress(pg);
                                }
                            });
                        } else if (read < 0) {
                            break;
                        }
                    } while (!isCancelled());
                } finally {
                    fout.close();
                }
                if (!isCancelled()) {
                    final byte[] sha1h = sha1.digest();
                    final int freadsofar = readSoFar;
                    SwingUtilities.invokeLater(new Runnable() {

                        @Override
                        public void run() {
                            DownloadProgress pg = new DownloadProgress();
                            pg.bytesReceived = freadsofar;
                            pg.startTimestamp = t0;
                            pg.bytesTotal = bt;
                            callback.success(pg, sha1h);
                        }
                    });
                } else {
                    SwingUtilities.invokeLater(new Runnable() {

                        @Override
                        public void run() {
                            callback.cancelled();
                        }
                    });
                }
            } finally {
                in.close();
            }
        } catch (final IOException ex) {
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    callback.failed(ex);
                }
            });
        }
        return null;
    }
