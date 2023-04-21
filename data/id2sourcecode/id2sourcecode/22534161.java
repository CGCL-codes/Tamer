    private void processURL(URL url) {
        try {
            MsgLog.message("DapSpider.processURL(): Processing url: " + url);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            if (connection == null) {
                MsgLog.error("Could not establish connection to URL");
            } else {
                String contentType = connection.getContentType();
                if (contentType == null) {
                    MsgLog.warning("DapSpider.processURL(): URL " + url + " is of unknown type");
                } else if ((contentType.indexOf("text") == 0) || (contentType.indexOf("html") >= 0)) {
                    MsgLog.mumble("DapSpider.processURL(): URL is of type html or text");
                    InputStream is = connection.getInputStream();
                    ParserFactory factory = ParserFactory.createFactory();
                    DapParser parser = factory.createParser(spider_type, this, baseURL, url.toString(), report, dataset, filenameRegex, baseFileRegex);
                    parser.parse(is);
                } else {
                    MsgLog.warning("DapSpider.processURL(): URL " + url + " is not of type text/html");
                }
            }
        } catch (IOException e) {
            workloadWaiting.remove(url);
            workloadError.add(url);
            MsgLog.error("Error with processing URL: " + url + " Exception thrown: " + e.toString());
            return;
        } catch (InvalidParserTypeException e) {
            workloadWaiting.remove(url);
            workloadError.add(url);
            MsgLog.error("Error with processing URL: " + url + " Exception thrown: " + e.toString());
            return;
        }
        workloadWaiting.remove(url);
        workloadProcessed.add(url);
        MsgLog.mumble("Complete: " + url);
        report.count(0);
    }
