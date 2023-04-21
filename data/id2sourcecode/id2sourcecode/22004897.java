    public String getAuthInfo(String ip) throws IOException {
        String auth = authurl + "?url_ver=Z39.88-2004&rft_id=info:lanl-repo/oppie&" + "svc_id=info:lanl-repo/svc/oppie/auth-getinst&" + "svc_val_fmt=http://oppie.lanl.gov/openurl/auth-getinst.html&svc.ip=" + ip;
        URL url = new URL(auth);
        HttpURLConnection huc = (HttpURLConnection) (url.openConnection());
        int code = huc.getResponseCode();
        StringBuffer sb = new StringBuffer();
        if (code == 200) {
            BufferedReader in = new BufferedReader(new InputStreamReader(huc.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                sb.append(inputLine);
            }
            in.close();
        }
        return sb.toString();
    }
