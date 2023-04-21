    private boolean downloadTimesAll() {
        String RID = Tools.getRID();
        try {
            RID = URLEncoder.encode(RID, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            System.out.println("Error: Can't build URL to Rachota Analytics server.");
            e.printStackTrace();
        }
        final String url_string = "http://rachota.sourceforge.net/getUsageTimes.php?rid=" + RID;
        usageTimesAll = "";
        final Thread connectionThread = new Thread("Rachota Analytics Download Times") {

            public void run() {
                try {
                    URL url = new URL(url_string);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    InputStream inputStream = connection.getInputStream();
                    while (true) {
                        int character = inputStream.read();
                        if (character == -1) break;
                        usageTimesAll = usageTimesAll + (char) character;
                    }
                    if (usageTimesAll.indexOf("Access denied.") != -1) {
                        usageTimesAll = "";
                    } else {
                        String start = "data: <b>";
                        String end = "</b><br>Records: ";
                        int indexStart = usageTimesAll.indexOf(start) + start.length();
                        int indexEnd = usageTimesAll.indexOf(end);
                        usageTimesAll = usageTimesAll.substring(indexStart, indexEnd);
                    }
                    connection.disconnect();
                } catch (Exception e) {
                    System.out.println("Error: Can't connect to Rachota Analytics server.");
                    usageTimesAll = null;
                }
            }
        };
        connectionThread.start();
        new Thread("Rachota Analytics Download Times killer") {

            public void run() {
                try {
                    sleep(30000);
                } catch (InterruptedException e) {
                }
                if (connectionThread.isAlive()) {
                    System.out.println("Error: Giving up...");
                    connectionThread.interrupt();
                }
            }
        }.start();
        while (connectionThread.isAlive()) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
            }
        }
        return (usageTimesAll != null);
    }
