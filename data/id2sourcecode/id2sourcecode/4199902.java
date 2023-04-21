    private String getContent(String strUrl) throws IOException {
        URL url = new URL(strUrl);
        BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
        String s = "";
        StringBuffer sb = new StringBuffer("");
        while ((s = br.readLine()) != null) {
            if (s.indexOf(checkMark) != -1) sb.append(s.trim().split("jar")[0]);
        }
        br.close();
        return sb.toString();
    }
