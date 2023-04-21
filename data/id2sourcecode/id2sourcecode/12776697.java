    public void fetch(String s) {
        URL url;
        try {
            url = new URL(s);
        } catch (MalformedURLException e) {
            System.exit(1);
            return;
        }
        done = false;
        timeoutThread = new Thread() {

            public void run() {
                try {
                    sleep(timeout);
                    if (!done) {
                        System.exit(2);
                    }
                } catch (InterruptedException e) {
                }
            }
        };
        timeoutThread.start();
        try {
            InputStream istream = new BufferedInputStream(url.openStream());
            OutputStream ostream = new BufferedOutputStream(System.out);
            for (; ; ) {
                int b = istream.read();
                if (b < 0) break;
                ostream.write(b);
            }
            ostream.close();
            System.out.flush();
            istream.close();
        } catch (IOException e) {
            System.exit(1);
        }
        done = true;
    }
