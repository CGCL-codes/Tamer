    public String getSite(URL url, String userAgent) {
        String urlS = url.toString();
        String data = (String) cachedData.get(urlS);
        if (data != null) return data;
        int pos = urlS.indexOf("&password=");
        if (pos == -1) pos = urlS.length();
        l.fine("Requesting " + urlS.substring(0, pos));
        try {
            URLConnection uc = url.openConnection();
            uc.setRequestProperty("User-Agent", userAgent);
            InputStream in = uc.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "ISO-8859-1"));
            StringBuffer sb = new StringBuffer();
            char[] buf = new char[4096];
            int len;
            while ((len = br.read(buf)) != -1) {
                sb.append(buf, 0, len);
            }
            String res = sb.toString();
            cachedData.put(urlS, res);
            l.finest("Done requesting.");
            return res;
        } catch (UnknownHostException ex) {
            l.warning(ex.toString());
            return "";
        } catch (IOException ex) {
            l.log(Level.SEVERE, "IOException", ex);
            return "";
        }
    }
