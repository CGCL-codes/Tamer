    public void run() {
        while (true) {
            log.debug("run: actions = " + action.size());
            for (Enumeration e = action.elements(); e.hasMoreElements(); ) {
                try {
                    String urlstring = url + "?" + e.nextElement();
                    System.out.println("opening " + urlstring);
                    URL u = new URL(urlstring);
                    HttpURLConnection urlConn = (HttpURLConnection) u.openConnection();
                    urlConn.connect();
                    int respCode = urlConn.getResponseCode();
                    if (respCode <= 100 || respCode >= 300) {
                        log.debug(urlstring + " failed: " + respCode);
                    }
                    sleep(1000);
                } catch (java.io.IOException e1) {
                    log.error("run: ", e1);
                } catch (InterruptedException e1) {
                    log.error("run: ", e1);
                }
            }
            try {
                log.debug("Sleeping for " + sleep);
                sleep(sleep);
            } catch (InterruptedException x) {
                log.fatal("run halted: " + x);
                System.exit(1);
            }
        }
    }
