    public String getMediaURL(String strLink) {
        try {
            String res = HTTP.post("http://megaupload.net/keepvid.php", "url=" + URLEncoder.encode(strLink, "UTF-8") + "&site=aa");
            System.out.println(res);
            String regexp = "http:\\/\\/[^\"]+\\/get_video[^\"]+";
            Pattern p = Pattern.compile(regexp);
            Matcher m = p.matcher(res);
            m.find();
            String strRetUrl = res.substring(m.start(), m.end());
            strRetUrl += "&fmt=18";
            try {
                URL url = new URL(strRetUrl);
                URLConnection conn = url.openConnection();
                InputStream in = conn.getInputStream();
                in.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                strRetUrl = strRetUrl.substring(0, strRetUrl.length() - 7);
            }
            System.out.println(strRetUrl);
            return strRetUrl;
        } catch (UnsupportedEncodingException e) {
            System.out.println("Error in Youtube Media Provider. Encoding is not supported. (How would that happen?!)");
            e.printStackTrace();
        }
        return "";
    }
