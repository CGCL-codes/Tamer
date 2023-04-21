    public void testCompareSpeeds() throws IOException, ScanException, PolicyException {
        String urls[] = { "http://slashdot.org/", "http://www.fark.com/", "http://www.cnn.com/", "http://google.com/", "http://www.microsoft.com/en/us/default.aspx", "http://deadspin.com/" };
        double totalDomTime = 0;
        double totalSaxTime = 0;
        int testReps = 15;
        for (int i = 0; i < urls.length; i++) {
            URL url = new URL(urls[i]);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());
            StringBuilder out = new StringBuilder();
            char[] buffer = new char[5000];
            int read = 0;
            do {
                read = in.read(buffer, 0, buffer.length);
                if (read > 0) {
                    out.append(buffer, 0, read);
                }
            } while (read >= 0);
            in.close();
            String html = out.toString();
            System.out.println("About to scan: " + url + " size: " + html.length());
            if (html.length() > policy.getMaxInputSize()) {
                System.out.println("   -Maximum input size exceeded. SKIPPING.");
                continue;
            }
            double domTime = 0;
            double saxTime = 0;
            for (int j = 0; j < testReps; j++) {
                domTime += as.scan(html, policy, AntiSamy.DOM).getScanTime();
                saxTime += as.scan(html, policy, AntiSamy.SAX).getScanTime();
            }
            domTime = domTime / testReps;
            saxTime = saxTime / testReps;
            totalDomTime += domTime;
            totalSaxTime += saxTime;
        }
        System.out.println("Total DOM time: " + totalDomTime);
        System.out.println("Total SAX time: " + totalSaxTime);
    }
