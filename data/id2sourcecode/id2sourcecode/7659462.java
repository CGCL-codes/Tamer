    public HttpResponse execute() throws SocketTimeoutException, ClientProtocolException, IOException {
        long start = System.currentTimeMillis();
        HttpUriRequest request = null;
        List<NameValuePair> postParams = new ArrayList<NameValuePair>();
        if (Method.POST == getMethod()) {
            HttpPost post = new HttpPost(getUrl());
            if (getQueries() != null) {
                for (Map.Entry<String, String> entry : getQueries().entrySet()) {
                    postParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                }
            }
            post.setEntity(new UrlEncodedFormEntity(postParams, getEncoding()));
            request = post;
        } else {
            StringBuilder sb = new StringBuilder(getUrl());
            if (getQueries() != null) {
                if (sb.indexOf("?") >= 0) {
                    sb.append('&');
                } else {
                    sb.append('?');
                }
                sb.append(makeQueryString(getQueries()));
            }
            String fullUrl = sb.toString();
            HttpGet get = new HttpGet(fullUrl);
            request = get;
        }
        if (getCookies() != null) {
            String cookieStr = makeCookieString(getCookies());
            request.addHeader("Cookie", cookieStr);
        }
        if (getHeaders() != null) {
            for (Map.Entry<String, String> entry : getHeaders().entrySet()) {
                request.addHeader(entry.getKey(), URLEncoder.encode(entry.getValue(), getEncoding()));
            }
        }
        HttpClient client = new DefaultHttpClient();
        client.getParams().setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, getConnectionTimeout());
        client.getParams().setIntParameter(CoreConnectionPNames.SO_TIMEOUT, getSoTimeout());
        org.apache.http.HttpResponse hr = null;
        HttpResponse res = new HttpResponse();
        hr = client.execute(request);
        res.setStatusCode(hr.getStatusLine().getStatusCode());
        for (Header header : hr.getAllHeaders()) {
            res.setHeader(header.getName(), header.getValue());
        }
        for (Header header : hr.getHeaders("Set-Cookie")) {
            String cookieEntry = header.getValue();
            Cookie cookie = Cookie.parseCookie(cookieEntry, null);
            res.setCookie(cookie.getKey(), cookie);
        }
        if (hr.getEntity() == null) {
            return res;
        }
        Header contentTypeHeader = hr.getEntity().getContentType();
        if (contentTypeHeader != null) {
            String contentType = contentTypeHeader.getValue();
            if (contentType != null) {
                Pattern p = Pattern.compile("text/[^;]+(\\s*;\\s*charset\\s*=\\s*(.+))?", Pattern.CASE_INSENSITIVE);
                Matcher m = p.matcher(contentType);
                if (m.find() && !StringUtils.isBlank(m.group(2))) {
                    res.setCharacterEncoding(m.group(2));
                }
            }
        }
        setContentLength(hr.getEntity().getContentLength());
        res.setContentLength(hr.getEntity().getContentLength());
        ByteArrayOutputStream baos = null;
        InputStream is = null;
        try {
            baos = new ByteArrayOutputStream();
            is = hr.getEntity().getContent();
            byte[] buff = new byte[getBufferSize()];
            int readSize = 0;
            while (!isCanceled()) {
                if (Thread.interrupted()) {
                    Thread.currentThread().interrupt();
                    synchronized (this) {
                        isCanceled = true;
                    }
                    break;
                }
                if ((readSize = is.read(buff)) < 0) {
                    break;
                }
                synchronized (this) {
                    setDownloaded(getDownloaded() + readSize);
                    setElapsed(System.currentTimeMillis() - start);
                    long elapsedSec = getElapsed() / 1000;
                    if (elapsedSec == 0) {
                        setAvarageSpeed(getDownloaded());
                    } else {
                        setAvarageSpeed(getDownloaded() / elapsedSec);
                    }
                }
                baos.write(buff, 0, readSize);
            }
            Log.v(TAG, "downloaded: avarage speed = " + getAvarageSpeed() + " bps, downloaded = " + getDownloaded() + " bytes");
            if (isCanceled()) {
                request.abort();
            }
        } finally {
            try {
                baos.close();
            } catch (Exception ignore) {
            }
            try {
                is.close();
            } catch (Exception ignore) {
            }
        }
        res.setBody(baos.toByteArray());
        return res;
    }
