    public List<GHMBean> parse(String url) throws Exception {
        List<GHMBean> l = new ArrayList<GHMBean>();
        GHMBean currentBean = null;
        HttpHost proxy = new HttpHost("172.16.1.138", 3128, "http");
        HttpClient httpclient = new DefaultHttpClient();
        if (activerProxyGL) {
            httpclient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
        }
        HttpGet httpget = new HttpGet(url);
        HttpResponse response = httpclient.execute(httpget);
        HttpEntity entity = response.getEntity();
        String[] lignes = EntityUtils.toString(entity).split("\n");
        String ligne;
        String date = null;
        String girl = null;
        log.info("Parse html...");
        for (int j = 0; j < lignes.length; j++) {
            ligne = lignes[j];
            log.debug(ligne);
            try {
                if (ligne.indexOf("<h2 class='date-header'") != -1) {
                    date = ligne.substring(ligne.indexOf("'>") + 2, ligne.indexOf("</"));
                    log.debug("Date : " + date);
                } else if (ligne.indexOf(".jpg") != -1 || ligne.indexOf(".JPG") != -1 || ligne.indexOf(".jpeg") != -1) {
                    String[] tab = ligne.split("\"");
                    for (int i = 0; i < tab.length; i++) {
                        if (tab[i].trim().startsWith("http") && (tab[i].trim().endsWith(".jpg") || tab[i].trim().endsWith(".JPG") || tab[i].trim().endsWith(".jpeg"))) {
                            log.debug(tab[i]);
                            if (tab[i].indexOf("s1600") != -1) {
                                if (currentBean.getImg1() == null) {
                                    currentBean.setImg1(tab[i]);
                                } else {
                                    currentBean.addReponse(tab[i]);
                                }
                            }
                        }
                    }
                } else if (ligne.indexOf("Girl #") != -1 && !ligne.startsWith("<li>")) {
                    currentBean = new GHMBean();
                    l.add(currentBean);
                    log.debug(ligne);
                    girl = ligne.substring(ligne.indexOf("'>") + 2, ligne.indexOf("</"));
                    log.info("Post : " + girl + " du " + date);
                    currentBean.setLibelle(girl + " du " + date);
                }
            } catch (Exception e) {
                log.error("ERREUR : ...");
            }
        }
        return l;
    }
