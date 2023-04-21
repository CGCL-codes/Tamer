    public GridInfo queryGridInfo(GridInfo grid) throws Exception {
        GridInfo info = null;
        HttpClient client = new DefaultHttpClient();
        HttpGet getMethod = new HttpGet(new URI(grid.loginuri + GRID_INFO_PROTOCOL));
        try {
            HttpResponse response = client.execute(getMethod);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    InputStream stream = entity.getContent();
                    String charset = null;
                    if (entity.getContentType() != null) {
                        HeaderElement values[] = entity.getContentType().getElements();
                        if (values.length > 0) {
                            NameValuePair param = values[0].getParameterByName("charset");
                            if (param != null) {
                                charset = param.getValue();
                            }
                        }
                    }
                    if (charset == null) {
                        charset = HTTP.DEFAULT_CONTENT_CHARSET;
                    }
                    XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
                    parser.setInput(stream, charset);
                    parser.nextTag();
                    parser.require(XmlPullParser.START_TAG, null, GRIDINFO);
                    if (!parser.isEmptyElementTag()) {
                        parser.nextTag();
                        info = parseRecord(parser);
                    }
                }
            }
        } finally {
            getMethod.abort();
        }
        if (info != null) {
            info.merge(grid);
        }
        return info;
    }
