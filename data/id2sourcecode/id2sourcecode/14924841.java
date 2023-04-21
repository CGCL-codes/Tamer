    public void runTest() {
        ClientConnectionManager ccm = new ThreadSafeClientConnManager(defaultParameters, supportedSchemes);
        DefaultHttpClient client = new DefaultHttpClient(ccm, defaultParameters);
        client.setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy());
        HttpEntity entity = null;
        ResponseHandler<String> responseHandler = null;
        try {
            BasicHttpContext localContext = new BasicHttpContext();
            CookieStore cookieStore = new BasicCookieStore();
            localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
            Header sessionHeader = null;
            if (this.loginAsUrl != null) {
                String loginAsUri = this.host + this.loginAsUrl;
                String loginAsParamString = "?" + this.loginAsUserParam + "&" + this.loginAsPasswordParam;
                HttpGet req2 = new HttpGet(loginAsUri + loginAsParamString);
                System.out.println("loginAsUrl:" + loginAsUri + loginAsParamString);
                req2.setHeader("Connection", "Keep-Alive");
                HttpResponse rsp = client.execute(req2, localContext);
                Header[] headers = rsp.getAllHeaders();
                for (int i = 0; i < headers.length; i++) {
                    Header hdr = headers[i];
                    String headerValue = hdr.getValue();
                    if (headerValue.startsWith("JSESSIONID")) {
                        sessionHeader = hdr;
                    }
                    System.out.println("login: " + hdr.getName() + " : " + hdr.getValue());
                }
                List<Cookie> cookies = cookieStore.getCookies();
                System.out.println("cookies.size(): " + cookies.size());
                for (int i = 0; i < cookies.size(); i++) {
                    System.out.println("Local cookie(0): " + cookies.get(i));
                }
            }
            if (HttpHandleMode.equals(this.responseHandlerMode)) {
            } else {
                responseHandler = new JsonResponseHandler(this);
            }
            String paramString = urlEncodeArgs(this.inMap, false);
            String thisUri = null;
            if (sessionHeader != null) {
                String sessionHeaderValue = sessionHeader.getValue();
                int pos1 = sessionHeaderValue.indexOf("=");
                int pos2 = sessionHeaderValue.indexOf(";");
                String sessionId = sessionHeaderValue.substring(pos1 + 1, pos2);
                thisUri = this.host + this.requestUrl + ";jsessionid=" + sessionId + "?" + paramString;
            } else {
                thisUri = this.host + this.requestUrl + "?" + paramString;
            }
            System.out.println("thisUri: " + thisUri);
            HttpGet req = new HttpGet(thisUri);
            if (sessionHeader != null) {
                req.setHeader(sessionHeader);
            }
            String responseBody = client.execute(req, responseHandler, localContext);
        } catch (HttpResponseException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (entity != null) entity.consumeContent();
            } catch (IOException e) {
                System.out.println("in 'finally'  " + e.getMessage());
            }
        }
        return;
    }
