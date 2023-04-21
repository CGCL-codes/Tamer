    public Document getWebPage(URL url) {
        URL targetUrl = url;
        Document document = null;
        long retries = 0;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(false);
        factory.setNamespaceAware(false);
        factory.setIgnoringElementContentWhitespace(true);
        InputStream urlStream = null;
        while (true) {
            try {
                URLConnection urlConnection = targetUrl.openConnection();
                urlConnection.setAllowUserInteraction(false);
                urlConnection.setConnectTimeout(CONNECTION_TIMEOUT);
                urlConnection.setReadTimeout(READ_TIMEOUT);
                Thread.yield();
                urlConnection.connect();
                urlStream = urlConnection.getInputStream();
                String type = urlConnection.getContentType();
                if (type != null && type.startsWith("text/html")) {
                    DocumentBuilder builder = factory.newDocumentBuilder();
                    document = builder.parse(urlStream);
                    urlStream.close();
                } else {
                    if (type != null) {
                        System.out.println("URL Type: '" + type + "' for '" + targetUrl.getPath() + "'");
                    } else {
                        System.out.println("URLConnection Type is null for '" + targetUrl.getPath() + "'");
                    }
                }
                Thread.yield();
                break;
            } catch (IOException e) {
                e.printStackTrace();
                if (urlStream != null) {
                    try {
                        urlStream.close();
                    } catch (IOException e1) {
                    }
                }
                if (retries < MAX_RETRY_ATTEMPTS) {
                    System.out.println(targetUrl.getPath() + " IOException, retry attempt: " + retries);
                    retries++;
                    try {
                        Thread.sleep(retries * RETRY_PERIOD);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                    continue;
                } else {
                    break;
                }
            } catch (SAXException e) {
                e.printStackTrace();
                if (retries < MAX_RETRY_ATTEMPTS) {
                    System.out.println(targetUrl.getPath() + " SAXException, retry attempt: " + retries);
                    retries++;
                    try {
                        Thread.sleep(retries * RETRY_PERIOD);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                    continue;
                } else {
                    break;
                }
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
                break;
            }
        }
        return document;
    }
