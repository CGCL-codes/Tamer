    public Response get(String path, List parameters) throws IOException, SAXException {
        URL url = UrlUtilities.buildUrl(getHost(), getPort(), path, parameters);
        if (Flickr.debugRequest) System.out.println("GET: " + url);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        if (proxyAuth) {
            conn.setRequestProperty("Proxy-Authorization", "Basic " + getProxyCredentials());
        }
        conn.connect();
        InputStream in = null;
        try {
            if (Flickr.debugStream) {
                in = new DebugInputStream(conn.getInputStream(), System.out);
            } else {
                in = conn.getInputStream();
            }
            Response response = null;
            synchronized (mutex) {
                Document document = builder.parse(in);
                response = (Response) responseClass.newInstance();
                response.parse(document);
            }
            return response;
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } finally {
            IOUtilities.close(in);
        }
    }
