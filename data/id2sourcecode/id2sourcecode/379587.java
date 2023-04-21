    public void startSelling(int Count, int Share, int Max) throws Exception {
        String sParams = getSellParams(Count, Share, Max);
        URL url = new URL(sSideURL + sSellURL);
        URLConnection conn = (URLConnection) url.openConnection();
        setRequestProperties(conn);
        conn.setDoOutput(true);
        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
        wr.write(sParams);
        wr.flush();
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line = rd.readLine();
        while (line != null) {
            sb.append(line + "\n");
            line = rd.readLine();
        }
        wr.close();
        rd.close();
        String sPage = sb.toString();
        Pattern p = Pattern.compile("\'Hinweis\', \'");
        Matcher matcher = p.matcher(sPage);
        sellSuccess = matcher.find();
    }
