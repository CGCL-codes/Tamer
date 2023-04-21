    @Override
    public String getOutStr(String urlstrMain, String urlStrRC, String User_Agent, String locale, String themes, String domain, String pathinfo) throws JSONException {
        log.warning("new request urlstrMain" + urlstrMain + " domain " + domain + " path " + pathinfo);
        try {
            URL url = new URL(urlstrMain);
            connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(20000);
            connection.setRequestProperty("User_Agent", User_Agent);
            connection.setRequestProperty("locale", locale);
            connection.setRequestProperty("themes", themes);
            connection.setRequestProperty("domain", domain);
            connection.setRequestProperty("pathinfo", pathinfo);
            rd = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF8"));
            sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            CreateCrContextImpl createCrContext = new CreateCrContextImpl();
            outstr = createCrContext.makeFrom(domain, pathinfo, sb.toString());
            if (outstr != null && outstr.length() > 200) {
                return outstr;
            } else {
                log.severe("Cant return outst");
            }
        } catch (IOException e) {
            log.severe(e.getMessage());
        } finally {
            connection.disconnect();
            rd = null;
            sb = null;
            outstr = null;
        }
        return null;
    }
