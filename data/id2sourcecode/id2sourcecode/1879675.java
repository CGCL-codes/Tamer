    public ForSpidersReq(String urlstr, String domain, String pathinfo, String locale, String links) throws IOException {
        URL url = new URL(urlstr);
        URLConnection conn = url.openConnection();
        conn.setRequestProperty("pathinfo", pathinfo);
        conn.setRequestProperty("domain", domain);
        conn.setRequestProperty("locale", locale);
        conn.setRequestProperty("links", links);
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF8"));
            StringBuffer response = new StringBuffer();
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            jsonContectResult = response.toString();
        } catch (SocketTimeoutException e) {
            log.severe("SoketTimeout NO!! RC  try again !!" + e.getMessage());
            jsonContectResult = null;
        } catch (Exception e) {
            log.severe("Except Rescue Start !! RC try again!! " + e.getMessage());
            jsonContectResult = null;
        }
    }
