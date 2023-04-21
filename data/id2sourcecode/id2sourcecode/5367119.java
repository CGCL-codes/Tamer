    public static String getPage(URL url, Proxy proxy) {
        try {
            StringBuffer ret = new StringBuffer("");
            URLConnection connection = (proxy == null) ? url.openConnection() : url.openConnection(proxy);
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = null;
            while ((line = br.readLine()) != null) {
                ret.append(line + StringUtil.CRLF);
            }
            return ret.toString();
        } catch (IOException e) {
            return null;
        }
    }
