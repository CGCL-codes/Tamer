        public void run() {
            for (; ; ) {
                try {
                    long time = System.currentTimeMillis();
                    URLConnection conn = url.openConnection();
                    conn.connect();
                    InputStream is = conn.getInputStream();
                    int thisSize = 0;
                    int size;
                    while ((size = is.read(buffer, 0, buffer.length)) > 0) {
                        thisSize += size;
                    }
                    time = System.currentTimeMillis() - time;
                    addData(thisSize, time);
                    is.close();
                } catch (IOException e) {
                    err(e.getMessage());
                }
            }
        }
