    public void setIdAndCat(String cat, int id) {
        URL url;
        HttpURLConnection conn;
        try {
            url = new URL("http://cri-srv-ade.insa-toulouse.fr:8080/ade/standard/gui/tree.jsp?category=" + cat + "&selectId=" + id);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestProperty("Cookie", sessionId);
            i = conn.getInputStream();
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        } catch (ProtocolException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
