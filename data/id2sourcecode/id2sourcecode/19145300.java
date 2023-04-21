    private String recupererVraiURL(String url) throws Exception {
        String ui = null;
        try {
            BufferedReader br = null;
            InputStream httpStream = null;
            log.info("URL : " + url);
            URL fileURL = new URL(url);
            URLConnection urlConnection = fileURL.openConnection();
            httpStream = urlConnection.getInputStream();
            br = new BufferedReader(new InputStreamReader(httpStream, "UTF-8"));
            String ligne;
            while ((ligne = br.readLine()) != null) {
                if (ligne.indexOf("<img") != -1) {
                    ui = ligne.substring(ligne.indexOf("src=\"") + 5, ligne.indexOf("\"", ligne.indexOf("src=\"") + 10));
                    log.debug("Vrai URL = " + ui);
                }
            }
            br.close();
            if (httpStream != null) {
                httpStream.close();
            }
            if (ui == null) {
                return url;
            }
        } catch (MalformedURLException e) {
            log.error("MalformedURL : " + url);
            throw e;
        }
        return ui;
    }
