        private void startDownload() {
            new Thread(new Runnable() {

                private double TRESHOLD = 1024 * 1024;

                public void run() {
                    try {
                        raFile = new RandomAccessFile(tempFile, "rwd");
                        URLConnection conn = remoteUrl.openConnection();
                        is = conn.getInputStream();
                        bis = new BufferedInputStream(is);
                        byte[] buffer = new byte[64000];
                        raFile.setLength(conn.getContentLength());
                        System.out.println("File Size: " + conn.getContentLength());
                        TRESHOLD = conn.getContentLength() * 0.03;
                        raFile.getChannel().force(true);
                        raFile.seek(0);
                        int read = 0;
                        double counter = 0;
                        while ((read = bis.read(buffer, 0, 64000)) != -1) {
                            raFile.write(buffer, 0, read);
                            counter += read;
                            if (counter > TRESHOLD) {
                                canStart = true;
                            }
                        }
                        bis.close();
                        raFile.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, "RemoteFileProxy: " + remoteUrl).start();
        }
