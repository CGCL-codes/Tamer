    public WidgetInstance getOrCreateInstance(Widget widget) throws SimalException {
        StringBuffer data = new StringBuffer();
        try {
            data.append(URLEncoder.encode("api_key", "UTF-8"));
            data.append("=");
            data.append(URLEncoder.encode(getApiKey(), "UTF-8"));
            data.append("&");
            data.append(URLEncoder.encode("userid", "UTF-8"));
            data.append("=");
            data.append(URLEncoder.encode(getUserID(), "UTF-8"));
            data.append("&");
            data.append(URLEncoder.encode("shareddatakey", "UTF-8"));
            data.append("=");
            data.append(URLEncoder.encode(getSharedDatakey(), "UTF-8"));
            data.append("&");
            data.append(URLEncoder.encode("widgetid", "UTF-8"));
            data.append("=");
            data.append(URLEncoder.encode(widget.getIdentifier(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new SimalException("UTF-8 encoding must be supported on the server", e);
        }
        URL url;
        WidgetInstance instance;
        OutputStreamWriter wr = null;
        InputStream is = null;
        try {
            url = new URL(getURL() + "/widgetinstances");
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data.toString());
            wr.flush();
            conn.setReadTimeout(1000);
            is = conn.getInputStream();
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            instance = widget.addInstance(db.parse(is));
        } catch (MalformedURLException e) {
            throw new RuntimeException("URL for supplied Wookie Server is malformed", e);
        } catch (ParserConfigurationException e) {
            throw new SimalException("Unable to configure XML parser", e);
        } catch (SAXException e) {
            throw new SimalException("Problem parsing XML from Wookie Server", e);
        } catch (IOException e) {
            throw new SimalException("Problem parsing XML from Wookie Server", e);
        } finally {
            if (wr != null) {
                try {
                    wr.close();
                } catch (IOException e) {
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
        }
        return instance;
    }
