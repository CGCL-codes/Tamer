    private static void testHtStartUrl(JTextArea log) {
        try {
            log.append("Step 2/" + steps + " - testing initial Hattrick connection..." + LS);
            final URL url = new URL(URL_SERVERS);
            final HttpURLConnection httpurlconnection = (HttpURLConnection) url.openConnection();
            httpurlconnection.setRequestMethod("GET");
            httpurlconnection.connect();
            InputStream is = httpurlconnection.getInputStream();
            final BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            StringBuffer sb = new StringBuffer();
            String line = null;
            boolean found = false;
            while ((line = br.readLine()) != null) {
                found = true;
                sb.append(line);
            }
            br.close();
            if (found) {
                Document doc = null;
                doc = XMLManager.instance().parseString(sb.toString());
                try {
                    String htserver = parseDetails(doc, log);
                    log.append("Test OK, Recommended HT server: " + htserver + LS);
                } catch (Exception e2) {
                    log.append("ERROR!" + LS);
                    log.append("Error details: " + e2 + LS);
                }
            } else {
                log.append("Test failed - no data!" + LS);
            }
        } catch (Exception e) {
            log.append("ERROR!" + LS);
            if (log != null) log.append("Error details: " + e + LS);
        }
    }
