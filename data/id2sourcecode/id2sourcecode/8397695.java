    public void harvest(String requestURL) throws IOException, ParserConfigurationException, SAXException, TransformerException {
        this.requestURL = requestURL;
        logger.debug("requestURL=" + requestURL);
        InputStream in = null;
        URL url = new URL(requestURL);
        HttpURLConnection con = null;
        int responseCode = 0;
        do {
            con = (HttpURLConnection) url.openConnection();
            con.setRequestProperty("User-Agent", "OAIHarvester/2.0");
            con.setRequestProperty("Accept-Encoding", "compress, gzip, identify");
            try {
                responseCode = con.getResponseCode();
                logger.debug("responseCode=" + responseCode);
            } catch (FileNotFoundException e) {
                logger.info(requestURL, e);
                responseCode = HttpURLConnection.HTTP_UNAVAILABLE;
            }
            if (responseCode == HttpURLConnection.HTTP_UNAVAILABLE) {
                long retrySeconds = con.getHeaderFieldInt("Retry-After", -1);
                if (retrySeconds == -1) {
                    long now = (new Date()).getTime();
                    long retryDate = con.getHeaderFieldDate("Retry-After", now);
                    retrySeconds = retryDate - now;
                }
                if (retrySeconds == 0) {
                    throw new FileNotFoundException("Bad URL?");
                }
                System.err.println("Server response: Retry-After=" + retrySeconds);
                if (retrySeconds > 0) {
                    try {
                        Thread.sleep(retrySeconds * 1000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        } while (responseCode == HttpURLConnection.HTTP_UNAVAILABLE);
        String contentEncoding = con.getHeaderField("Content-Encoding");
        logger.debug("contentEncoding=" + contentEncoding);
        if ("compress".equals(contentEncoding)) {
            ZipInputStream zis = new ZipInputStream(con.getInputStream());
            zis.getNextEntry();
            in = zis;
        } else if ("gzip".equals(contentEncoding)) {
            in = new GZIPInputStream(con.getInputStream());
        } else if ("deflate".equals(contentEncoding)) {
            in = new InflaterInputStream(con.getInputStream());
        } else {
            in = con.getInputStream();
        }
        InputSource data = new InputSource(in);
        Thread t = Thread.currentThread();
        DocumentBuilder builder = (DocumentBuilder) builderMap.get(t);
        if (builder == null) {
            builder = factory.newDocumentBuilder();
            builderMap.put(t, builder);
        }
        doc = builder.parse(data);
        StringTokenizer tokenizer = new StringTokenizer(getSingleString("/*/@xsi:schemaLocation"), " ");
        StringBuffer sb = new StringBuffer();
        while (tokenizer.hasMoreTokens()) {
            if (sb.length() > 0) sb.append(" ");
            sb.append(tokenizer.nextToken());
        }
        this.schemaLocation = sb.toString();
    }
