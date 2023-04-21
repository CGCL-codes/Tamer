    private String runQuery(String urlString, String term, String sourceLanguage, String targetLanguage) {
        try {
            String searchString = String.format(urlString, URLEncoder.encode(term, "UTF-8"), sourceLanguage, targetLanguage);
            URL lurl = new URL(searchString);
            URLConnection lconn = lurl.openConnection();
            lconn.setRequestProperty("User-Agent", "");
            lconn.setRequestProperty("Content-Type", "text/plain");
            lconn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(lconn.getOutputStream());
            wr.write("");
            wr.flush();
            BufferedReader lrd = new BufferedReader(new InputStreamReader(lconn.getInputStream(), "UTF-8"));
            StringBuilder lres = new StringBuilder();
            char[] lbuf = new char[2048];
            int lcount = 0;
            while ((lcount = lrd.read(lbuf)) != -1) {
                lres.append(lbuf, 0, lcount);
            }
            lrd.close();
            String queryresult = lres.toString();
            queryresult = queryresult.replaceAll(" +", " ");
            queryresult = queryresult.replaceAll("\\n+", "");
            queryresult = queryresult.replaceAll("\\r+", "");
            queryresult = queryresult.replaceAll("\\t+", "");
            queryresult = queryresult.replaceAll("<img.*?</img>", "");
            queryresult = queryresult.replaceAll("<img .*?>", "");
            queryresult = queryresult.replaceAll("<a .*?>", "");
            queryresult = queryresult.replaceAll("<tr .*?>", "<tr>");
            return queryresult;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
