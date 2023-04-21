    @Test
    public final void loginWikiMWLastSSLAndHtaccess() throws Exception {
        AbstractHttpClient httpClient = getSSLFakeHttpClient();
        Version latest = Version.getLatest();
        URL u = new URL(getValue("wiki_url_latest").replace("http", "https"));
        assertEquals("https", u.getProtocol());
        int port = 443;
        {
            HttpHost targetHost = new HttpHost(u.getHost(), port, u.getProtocol());
            HttpGet httpget = new HttpGet(u.getPath());
            HttpResponse resp = httpClient.execute(targetHost, httpget);
            assertEquals(401, resp.getStatusLine().getStatusCode());
            resp.getEntity().consumeContent();
        }
        httpClient.getCredentialsProvider().setCredentials(new AuthScope(u.getHost(), port), new UsernamePasswordCredentials(BotFactory.getWikiUser(latest), BotFactory.getWikiPass(latest)));
        HttpActionClient sslFakeClient = new HttpActionClient(httpClient, u);
        bot = new MediaWikiBot(sslFakeClient);
        bot.login(BotFactory.getWikiUser(latest), BotFactory.getWikiPass(latest));
        assertTrue(bot.isLoggedIn());
    }
