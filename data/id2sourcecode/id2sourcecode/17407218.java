    private List<Person> parseURL(String webPage) {
        visitedSites.add(webPage);
        URL url;
        List<Person> people;
        try {
            url = new URL(webPage);
        } catch (MalformedURLException e1) {
            return null;
        }
        HttpURLConnection httpConn = null;
        try {
            httpConn = (HttpURLConnection) url.openConnection();
            httpConn.setConnectTimeout(1000);
            XMLStreamReader2 staxXmlReader = (XMLStreamReader2) factory.createXMLStreamReader(httpConn.getInputStream());
            people = parsePeople(staxXmlReader, webPage);
        } catch (SocketTimeoutException c) {
            return null;
        } catch (ConnectException c) {
            return null;
        } catch (IOException e) {
            return null;
        } catch (XMLStreamException e) {
            return null;
        } finally {
            if (httpConn != null) httpConn.disconnect();
        }
        System.out.println(saveRDFs + " ok:" + (people != null));
        if (people != null) {
            try {
                Thread.sleep(250);
            } catch (InterruptedException e2) {
                e2.printStackTrace();
            }
        }
        if (saveRDFs && people != null) {
            String dataDirectory = pa.getInputDir();
            if (!dataDirectory.endsWith("[/|\\]")) dataDirectory = dataDirectory + File.separator;
            new File(dataDirectory + "rdfs").mkdir();
            String filename = dataDirectory + "rdfs" + File.separator + "FOAF_" + files + ".rdf";
            download(webPage, filename);
            validRdfFileToURL.put(filename, webPage);
        }
        return people;
    }
