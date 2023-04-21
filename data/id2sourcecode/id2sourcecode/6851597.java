    public static void main(String[] args) {
        final int[] finished = { 0 };
        try {
            Collection<SwingWorker> sws = new ArrayList<SwingWorker>();
            for (int i = 0; i < 1; i++) {
                SwingWorker sw = new SwingWorker<String, String>() {

                    int total = 0;

                    long timeTotal = 0;

                    float totalTime = 0;

                    @Override
                    public String doInBackground() {
                        long time = System.currentTimeMillis();
                        timeTotal = System.currentTimeMillis();
                        try {
                            String result = icat.downloadDatafile(sessionId, datafileId);
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
                                byte[] buff = new byte[32 * 1024];
                                while ((s = is.read(buff)) != -1) {
                                    total += s;
                                }
                                totalTime = (System.currentTimeMillis() - time) / 1000f;
                                System.out.println("\nTime taken to download: " + totalTime + " seconds");
                            } finally {
                                try {
                                    if (is != null) is.close();
                                } catch (IOException ioe) {
                                }
                            }
                        } catch (Exception ex) {
                            System.out.println("Error with: " + ex);
                            ex.printStackTrace();
                        }
                        return "finished";
                    }

                    @Override
                    public void done() {
                        try {
                            get();
                            float totalTime = (System.currentTimeMillis() - timeTotal) / 1000f;
                            System.out.println("finished ok " + finished[0]++ + " : total " + total / 1024f / 1024f + " Mb, Total time: " + totalTime + " seconds");
                        } catch (Exception ex) {
                            finished[0]++;
                            System.out.println(ex.getMessage());
                        }
                    }
                };
                sws.add(sw);
            }
            int i = 0;
            Thread.sleep(2000);
            for (SwingWorker swingWorker : sws) {
                System.out.println("Starting" + i++);
                swingWorker.execute();
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
