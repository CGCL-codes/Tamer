    private String rechercherURLConnexionGuestAuth() throws IOException {
        String ui = null;
        BufferedReader br = null;
        InputStream httpStream = null;
        log.debug("Recherche dans : " + URL_START_PAGE);
        HttpHost proxy = new HttpHost("172.16.1.138", 3128, "http");
        HttpClient httpclient = new DefaultHttpClient();
        if (activerProxyGL) {
            httpclient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
        }
        HttpGet httpget = new HttpGet(URL_START_PAGE);
        HttpResponse response = httpclient.execute(httpget);
        HttpEntity entity = response.getEntity();
        String[] lignes = EntityUtils.toString(entity).split("\n");
        String ligne;
        for (int i = 0; i < lignes.length; i++) {
            log.debug(lignes[i]);
            if (lignes[i].indexOf("href=\"http://guesshermuff.blogspot.com/") != -1) {
                ui = lignes[i].substring(lignes[i].indexOf("href=\"") + 6, lignes[i].indexOf("\"", lignes[i].indexOf("href=\"") + 10));
                log.debug("URL Guest Auth = " + ui);
            }
        }
        log.debug("URL trouv� : " + ui);
        URL_GUEST_AUTH = ui.substring(ui.indexOf("guestAuth="));
        log.debug("GUEST_AUTH=" + URL_GUEST_AUTH);
        return ui;
    }
