    private static void testNormalUrl(JTextArea log) {
        try {
            log.append("Step 1/" + steps + " - testing normal connection..." + LS);
            final URL url = new URL(URL_NORMAL);
            final HttpURLConnection httpurlconnection = (HttpURLConnection) url.openConnection();
            httpurlconnection.setRequestMethod("GET");
            httpurlconnection.connect();
            InputStream is = httpurlconnection.getInputStream();
            final BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String line = null;
            boolean found = false;
            while ((line = br.readLine()) != null) {
                found = true;
                log.append("\t >>> " + line + LS);
            }
            br.close();
            if (found) {
                log.append("Test OK" + LS);
            } else {
                log.append("Test failed - no data!" + LS);
            }
        } catch (Exception e) {
            log.append("ERROR!" + LS);
            if (log != null) log.append("Error: " + e);
        }
    }
