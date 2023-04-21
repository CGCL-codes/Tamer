    public static void log(final String user, final String msg) {
        if (!Prefs.current.log || !enabled) return;
        Thread logger = new Thread() {

            public void run() {
                this.setPriority(Thread.MIN_PRIORITY);
                try {
                    URL url = new URL(Prefs.current.baseURL + "log.php?user=" + URLEncoder.encode(user, "utf8") + "&message=" + URLEncoder.encode(msg, "utf8"));
                    url.openStream().close();
                } catch (Exception e) {
                    System.out.println("Logger: " + e);
                }
            }
        };
        logger.start();
    }
