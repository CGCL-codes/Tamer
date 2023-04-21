    private void postDmpRecords() {
        LOGGER.info("st: POST updating " + dmpRecords.size() + " DmpAft packets");
        for (int i = 0; i < dmpRecords.size(); i++) {
            DmpRecord record = (DmpRecord) dmpRecords.get(i);
            if (record.isValid()) {
                try {
                    URL url = new URL("http://www.canterburyweather.co.uk/post.php");
                    URLConnection urlConn = url.openConnection();
                    urlConn.setDoOutput(true);
                    OutputStreamWriter out = new OutputStreamWriter(urlConn.getOutputStream());
                    String content = record.toPostRequest();
                    System.out.println(content);
                    out.write(content);
                    out.flush();
                    BufferedReader in = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
                    String line;
                    while ((line = in.readLine()) != null) {
                        if (LOGGER.isDebugEnabled()) {
                            LOGGER.debug(line);
                        }
                    }
                    in.close();
                    out.close();
                } catch (Exception e) {
                    LOGGER.error(e);
                }
            }
        }
        LOGGER.info("st: Done POST updating " + dmpRecords.size() + " DmpAft packets");
    }
