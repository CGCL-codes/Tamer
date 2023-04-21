    public String getLocationResponse(String phoneNo) {
        String mlpResp = null;
        try {
            URL url = new URL("http://127.0.0.1:8080/simu/thenahari?phoneNo=" + phoneNo);
            URLConnection conn = url.openConnection();
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            StringBuffer sb = new StringBuffer();
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append("\n" + line);
            }
            rd.close();
            mlpResp = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mlpResp;
    }
