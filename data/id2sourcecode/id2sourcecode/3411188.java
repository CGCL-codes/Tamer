    private TableModel fetchIndex(String index) throws IOException {
        URL url = new URL(MessageFormat.format(WEB_ADDRESS_FORMAT, URLEncoder.encode(index, "utf-8")));
        log.log("Request: " + url.toString());
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        TableModel model;
        try {
            model = parseCSVData(in, index);
        } catch (Exception e) {
            log.log(e.toString());
            model = null;
        }
        in.close();
        return model;
    }
