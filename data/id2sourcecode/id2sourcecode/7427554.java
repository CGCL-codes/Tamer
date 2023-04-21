    public void setWeek(int week) {
        week = week + 17;
        if (week > 52) {
            week = week - 52;
        }
        HttpURLConnection conn;
        URL url;
        try {
            url = new URL("http://cri-srv-ade.insa-toulouse.fr:8080/ade/custom/modules/plannings/info.jsp?week=" + week + "&reset=true");
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
