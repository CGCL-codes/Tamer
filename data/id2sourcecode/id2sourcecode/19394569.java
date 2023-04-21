    @Override
    public IpInfo get(String ip) {
        try {
            URL url = new URL("http://ipinfodb.com/ip_query.php?ip=" + ip);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuffer buffer = new StringBuffer();
                String temp = "";
                while (null != temp) {
                    buffer.append(temp);
                    temp = in.readLine();
                }
                return parse(ip, buffer.toString());
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
