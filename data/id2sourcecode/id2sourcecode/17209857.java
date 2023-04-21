    public static String getHtmlSource(String url) {
        StringBuffer codeBuffer = null;
        BufferedReader in = null;
        try {
            URLConnection uc = new URL(url).openConnection();
            uc.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows XP; DigExt)");
            in = new BufferedReader(new InputStreamReader(uc.getInputStream(), "gb2312"));
            codeBuffer = new StringBuffer();
            String tempCode = "";
            while ((tempCode = in.readLine()) != null) {
                codeBuffer.append(tempCode).append("\n");
            }
            in.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return codeBuffer.toString();
    }
