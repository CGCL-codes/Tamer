    public LocationResponse getResponse(LocationRequest lrq) throws UnregisteredComponentException {
        LocationResponse lrs = lrq.createResponse();
        try {
            String rqs, rss;
            rqs = encodeGearsRequest(lrq);
            URL url = new URL(this.gearsServerUri);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.addRequestProperty("Content-Type", "application/json");
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(rqs);
            wr.flush();
            BufferedReader rd;
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            rss = "";
            String line;
            while ((line = rd.readLine()) != null) {
                rss += line;
            }
            rd.close();
            decodeGearsResponse(rss, lrs);
        } catch (Exception e) {
            e.printStackTrace();
            lrs.setError("Error querying Gears");
        }
        return lrs;
    }
