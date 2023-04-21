    public String post(final String payload) throws Exception {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        if (authorization != null) connection.setRequestProperty("Authorization", authorization);
        connection.setDoOutput(true);
        OutputStream outs = null;
        try {
            outs = connection.getOutputStream();
        } catch (IOException ioe) {
            if (outs != null) outs.close();
            throw ioe;
        }
        PrintWriter out = new PrintWriter(outs);
        out.print(payload);
        out.close();
        int responseLen = connection.getContentLength();
        InputStream ins = null;
        try {
            ins = connection.getInputStream();
        } catch (IOException ioe) {
            if (ins != null) ins.close();
            throw ioe;
        }
        BufferedReader in = new BufferedReader(new InputStreamReader(ins));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) response.append(inputLine);
        in.close();
        return response.toString();
    }
