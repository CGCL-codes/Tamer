        @Override
        protected Void doInBackground() throws Exception {
            InputStream in = null;
            OutputStream out = null;
            try {
                URL url = new URL(downloadURL);
                URLConnection connection = url.openConnection();
                length = connection.getContentLength();
                in = (InputStream) connection.getContent();
                if (!(isCancelled())) {
                    out = new BufferedOutputStream(new FileOutputStream(filename));
                    byte[] buffer = new byte[1024];
                    int numRead;
                    long numWritten = 0;
                    while (!(isCancelled()) && ((numRead = in.read(buffer)) != -1)) {
                        out.write(buffer, 0, numRead);
                        numWritten += numRead;
                        int progressAt = (int) ((double) numWritten / length * 100);
                        publish(new Long(numWritten));
                        setProgress(progressAt);
                    }
                }
            } catch (MalformedURLException ex) {
                Logger.getLogger(DownloadWorker.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                ioerror = true;
            } finally {
                try {
                    if (in != null) {
                        in.close();
                    }
                    if (out != null) {
                        out.close();
                    }
                } catch (IOException ioe) {
                    ioerror = true;
                }
            }
            return null;
        }
