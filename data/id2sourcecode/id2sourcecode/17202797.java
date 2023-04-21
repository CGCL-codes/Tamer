                    @Override
                    public String doInBackground() {
                        long time = System.currentTimeMillis();
                        try {
                            String result = icat.downloadDataset(sessionId, datafileId);
                            java.lang.System.out.println("Result = " + result);
                            totalTime = (System.currentTimeMillis() - time) / 1000f;
                            System.out.println("\nTime taken to get URL back: " + totalTime + " seconds");
                            time = System.currentTimeMillis();
                            URL url = new URL(result);
                            InputStream is = null;
                            DataInputStream dis;
                            int s;
                            try {
                                is = url.openStream();
                                byte[] buff = new byte[4 * 1024];
                                while ((s = is.read(buff)) != -1) {
                                    total += s;
                                }
                                totalTime = (System.currentTimeMillis() - time) / 1000f;
                                System.out.println("\nTime taken to download: " + totalTime + " seconds");
                            } finally {
                                try {
                                    if (is != null) {
                                        is.close();
                                    }
                                } catch (IOException ioe) {
                                }
                            }
                        } catch (Exception ex) {
                            System.out.println("Error with: " + ex);
                            ex.printStackTrace();
                        }
                        return "finished";
                    }
