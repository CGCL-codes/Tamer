    public void fetch(M_joogoo account) throws IOException {
        if (account.getToken() == null) {
            throw new IOException("user token is null.");
        }
        M_service service = account.getMServiceRef().getModel();
        OAuthConsumer consumer = service.getConsumer();
        try {
            JSONObject token = new JSONObject(account.getToken());
            String key = token.getString("key");
            String secret = token.getString("secret");
            consumer.setTokenWithSecret(key, secret);
        } catch (JSONException e) {
            throw new RuntimeException("invalid state, userId=" + account.getUserId(), e);
        }
        String targetUrl = service.getTargetUrl("NOTIFICATION");
        URL url = new URL(targetUrl);
        HttpURLConnection c = (HttpURLConnection) url.openConnection();
        try {
            consumer.sign(c);
        } catch (OAuthMessageSignerException e) {
            throw new IOException(e.getMessage());
        } catch (OAuthExpectationFailedException e) {
            throw new IOException(e.getMessage());
        } catch (OAuthCommunicationException e) {
            throw new IOException(e.getMessage());
        }
        HttpTransport transport = new HttpTransport();
        transport.defaultHeaders.authorization = c.getRequestProperty("Authorization");
        FetcherService fetcher = FetcherServiceFactory.createFetcher(service);
        fetcher.setUpTransport(transport);
        List<T_new> news = fetcher.executeGet(transport, targetUrl);
        for (T_new n : news) {
            if (Datastore.getOrNull(T_new.class, n.getKey()) == null) {
                n.setUserId(account.getUserId());
                n.getMJoogooRef().setModel(account);
                Datastore.put(n);
            }
        }
    }
