    public TestHTTPConnection() {
        boolean failed = false;
        String proxyURL = "http://www-cache.leeds.ac.uk/";
        int proxyPort = 3128;
        try {
            System.out.println("Testing HTTP Connection without using a proxy");
            URL url = new URL("http://dl.dropbox.com/u/299787/test_csv.csv");
            HttpURLConnection uc = (HttpURLConnection) url.openConnection();
            uc.connect();
            BufferedInputStream bis = new BufferedInputStream(uc.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(bis));
            int lineCount = 0;
            String s;
            while ((s = reader.readLine()) != null && lineCount < 10) {
                System.out.println(s);
                lineCount++;
            }
            System.out.println("Finished.");
        } catch (IOException ex) {
            System.err.println("Failed: " + ex.getMessage());
            ex.printStackTrace();
            failed = true;
        }
        if (failed) {
            try {
                failed = false;
                System.out.println("Testing HTTP Connection using a proxy passed to the URL object");
                Proxy prxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyURL, proxyPort));
                URL url = new URL("http://dl.dropbox.com/u/299787/test_csv.csv");
                HttpURLConnection uc = (HttpURLConnection) url.openConnection(prxy);
                uc.connect();
                BufferedInputStream bis = new BufferedInputStream(uc.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(bis));
                int lineCount = 0;
                String s;
                while ((s = reader.readLine()) != null && lineCount < 10) {
                    System.out.println(s);
                    lineCount++;
                }
                System.out.println("Finished.");
            } catch (IOException ex) {
                System.err.println("Failed: " + ex.getMessage());
                ex.printStackTrace();
                failed = true;
            }
        }
        if (failed) {
            failed = false;
            try {
                System.setProperty("http.proxyHost", proxyURL);
                System.setProperty("http.proxyPort", String.valueOf(proxyPort));
                URL url = new URL("http://dl.dropbox.com/u/299787/test_csv.csv");
                HttpURLConnection uc = (HttpURLConnection) url.openConnection();
                uc.connect();
                BufferedInputStream bis = new BufferedInputStream(uc.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(bis));
                int lineCount = 0;
                String s;
                while ((s = reader.readLine()) != null && lineCount < 10) {
                    System.out.println(s);
                    lineCount++;
                }
            } catch (IOException ex) {
                System.err.println("Failed: " + ex.getMessage());
                ex.printStackTrace();
                failed = true;
            }
        }
        System.out.println("Finished.");
    }
