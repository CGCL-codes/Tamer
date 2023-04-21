    private String extractPageLinksFromWiki(final String pageName) {
        Integer numberOfLinks = 0;
        try {
            String data = URLEncoder.encode("action", "UTF-8") + "=" + URLEncoder.encode("query", "UTF-8");
            data += "&" + URLEncoder.encode("format", "UTF-8") + "=" + URLEncoder.encode("txt", "UTF-8");
            data += "&" + URLEncoder.encode("titles", "UTF-8") + "=" + URLEncoder.encode(pageName, "UTF-8");
            data += "&" + URLEncoder.encode("prop", "UTF-8") + "=" + URLEncoder.encode("links", "UTF-8");
            data += "&" + URLEncoder.encode("pllimit", "UTF-8") + "=" + URLEncoder.encode("500", "UTF-8");
            String plcontinue = "";
            while (plcontinue != null) {
                final URL url = new URL(HOST_API);
                final URLConnection urlConn = url.openConnection();
                urlConn.setDoOutput(true);
                urlConn.setDoInput(true);
                final OutputStreamWriter urlWriter = new OutputStreamWriter(urlConn.getOutputStream());
                urlWriter.write(data + plcontinue);
                urlWriter.flush();
                final BufferedReader urlReader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
                String line;
                plcontinue = null;
                while ((line = urlReader.readLine()) != null) {
                    if (line.contains("[title]")) {
                        numberOfLinks++;
                    }
                    if (line.contains("[plcontinue]")) {
                        plcontinue = "&" + URLEncoder.encode("plcontinue", "UTF-8") + "=" + URLEncoder.encode(line.substring(line.indexOf(" => ") + 4, line.length()), "UTF-8");
                    }
                }
                numberOfLinks--;
                urlReader.close();
                urlWriter.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return numberOfLinks + " links";
    }
