    public void startCollecting(CollectActivity collectActivity, int[] xy) throws Exception {
        String sParams = getCollectParams(collectActivity, xy[0], xy[1]);
        URL url = new URL(sSideURL + sCollectURL);
        URLConnection conn = (URLConnection) url.openConnection();
        ;
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
        Pattern p = Pattern.compile("Du bist auf Pfandflaschensuche");
        Matcher matcher = p.matcher(sPage);
        collectStarted = matcher.find();
    }
