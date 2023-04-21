    public static Document getXMLFromURL(java.net.URL url, boolean useCaching) {
        try {
            SAXBuilder builder = new SAXBuilder();
            if (useCaching) {
                File cachedXMLFile = getXMLFromCache(url);
                if (cachedXMLFile != null) {
                    try {
                        Document xml = builder.build(cachedXMLFile);
                        return xml;
                    } catch (Exception x) {
                        Config.log(INFO, "Cached XML could not be parsed, reading from online source...", x);
                    }
                }
            }
            File tempXMLFile = new File(Config.BASE_PROGRAM_DIR + "\\res\\temp.xml");
            if (tempXMLFile.exists()) tempXMLFile.delete();
            tempXMLFile.createNewFile();
            FileWriter fstream = null;
            BufferedWriter tempXML = null;
            fstream = new FileWriter(tempXMLFile);
            tempXML = new BufferedWriter(fstream);
            URLConnection urlConn = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
            while (true) {
                String line = in.readLine();
                if (line == null) break;
                tempXML.write(stripInvalidXMLChars(line) + LINE_BRK);
                tempXML.flush();
            }
            in.close();
            tempXML.close();
            if (useCaching) {
                cacheXML(url, tempXMLFile);
            }
            Document xml = builder.build(tempXMLFile);
            return xml;
        } catch (Exception x) {
            Config.log(ERROR, "Could not get valid XML data from URL: " + url, x);
            String stack = getStacktraceAsString(x).toLowerCase();
            if (stack.contains("server returned http response code: 999") && stack.contains("us.music.yahooapis.com")) {
                Config.SCRAPE_MUSIC_VIDEOS = false;
                Config.log(WARNING, "Disabling future Yahoo Music Video scraping because requests are over-limit (Response code 999).");
            }
            return null;
        }
    }
