    private void send(String message, String command) throws Exception {
        String url = PushUrl;
        DefaultHttpClient httpClient = new DefaultHttpClient();
        if (dnsCache) {
            InetAddress ipaddr;
            try {
                ipaddr = InetAddress.getByName(PushHost);
                this.pushaddr = ipaddr;
            } catch (UnknownHostException e) {
                if (pushaddr != null) {
                    log.warn("DNS lookup error, using cache...");
                    ipaddr = this.pushaddr;
                } else {
                    throw (e);
                }
            }
            SSLSocketFactory sf = SSLSocketFactory.getSocketFactory();
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            Scheme sc = new Scheme("https", sf, 443);
            httpClient.getConnectionManager().getSchemeRegistry().register(sc);
            url = "https://" + ipaddr.getHostAddress() + PushPath;
        }
        HttpPost post = new HttpPost(url);
        post.setHeader("Host", PushHost);
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("user_credentials", this.credentials));
        if (!this.sound.isEmpty()) {
            formparams.add(new BasicNameValuePair("notification[sound]", this.sound));
        }
        formparams.add(new BasicNameValuePair("notification[message]", message));
        formparams.add(new BasicNameValuePair("notification[icon_url]", this.iconUrl));
        formparams.add(new BasicNameValuePair("notification[message_level]", "2"));
        formparams.add(new BasicNameValuePair("notification[silent]", "0"));
        if (command != null && !command.isEmpty()) {
            formparams.add(new BasicNameValuePair("notification[action_loc_key]", "Reply"));
            formparams.add(new BasicNameValuePair("notification[run_command]", command));
        } else {
            formparams.add(new BasicNameValuePair("notification[action_loc_key]", ""));
        }
        UrlEncodedFormEntity entity = null;
        try {
            entity = new UrlEncodedFormEntity(formparams, "UTF-8");
        } catch (Exception e) {
        }
        post.setEntity(entity);
        try {
            HttpResponse res = httpClient.execute(post);
            int status = res.getStatusLine().getStatusCode();
            if (status != 200) {
                if (status >= 500) {
                    throw new MyHttpException("http server error. status=" + status);
                } else {
                    throw new Exception("http server error. status=" + status);
                }
            }
            String resString = EntityUtils.toString(res.getEntity());
            log.info("サーバ応答:" + resString);
            JSONObject json = JSONObject.fromObject(resString);
            int id = json.getInt("id");
            if (id < 1) {
                throw new Exception("illegal id returned");
            }
        } catch (JSONException e) {
            throw new Exception("wrong request");
        } finally {
            post.abort();
        }
    }
