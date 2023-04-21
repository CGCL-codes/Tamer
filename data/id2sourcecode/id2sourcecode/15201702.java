    @Override
    public String getContent(URL url) {
        try {
            URLConnection conn = url.openConnection();
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
        return jsonContectResult;
    }
