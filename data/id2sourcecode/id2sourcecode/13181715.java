    public HttpResponse post(String toFetchStr, String[][] headers, Map parameterMap, java.util.List<MemoryFileItem> items) throws ClientProtocolException, IOException {
        HttpClient httpClient = makeHTTPClient();
        String fetchUrl = null == toFetchStr ? "http://goo" + ".gl/service/search.html?searchTerm=jcrontab" : toFetchStr;
        HttpPost m = new HttpPost(fetchUrl);
        for (String[] nextHeader : headers) m.addHeader(nextHeader[0], nextHeader[1]);
        addCookies(m);
        m.addHeader("Host", m.getURI().getHost());
        if (items != null) {
            MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.STRICT);
            for (final MemoryFileItem item : items) {
                String contentType = item.getContentType();
                try {
                    contentType = contentType.substring(0, contentType.indexOf(";"));
                } catch (Exception e) {
                }
                String nameTmp = item.getName();
                final ContentBody contentBody = new InputStreamBody(item.getInputStream(), contentType, nameTmp);
                final StringBody comment = new StringBody("Filename:" + nameTmp);
                reqEntity.addPart(item.getFieldName(), contentBody);
                reqEntity.addPart("file#" + nameTmp, comment);
                m.setEntity(reqEntity);
            }
            for (Object nextParName : parameterMap.keySet()) {
                String parName = "" + nextParName;
                Object aString = parameterMap.get(parName);
                String valueTmp = ((String[]) aString)[0];
                final StringBody comment = new StringBody(valueTmp);
                reqEntity.addPart(parName, comment);
            }
        } else {
            validateContentType(parameterMap, m);
            HttpParams arg0 = httpClient.getParams();
            for (Object nextParName : parameterMap.keySet()) {
                String parName = "" + nextParName;
                Object aString = parameterMap.get(parName);
                String valueTmp = ((String[]) aString)[0];
                arg0.setParameter(parName, valueTmp);
            }
            m.setParams(arg0);
        }
        HttpResponse respTmp = httpClient.execute(m);
        respTmp = makeAuth(toFetchStr, httpClient, m, respTmp);
        StatusLine statusLine = respTmp.getStatusLine();
        String statusTmp = statusLine.toString();
        this.parseCookies(m, respTmp);
        if ("HTTP/1.1 302 Found".equals(statusTmp)) {
            String movedTo = "" + respTmp.getHeaders("Location")[0];
            movedTo = movedTo.substring("Location: ".length());
            String uri = m.getURI().toString();
            int domainUrlLen = uri.indexOf(m.getURI().getPath());
            movedTo = uri.substring(0, domainUrlLen) + movedTo;
            respTmp = get(movedTo, headers);
            respTmp.addHeader("X-MOVED", movedTo);
        }
        return respTmp;
    }
