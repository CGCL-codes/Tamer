    @Override
    public String httpCall(String urlstr, String input) {
        try {
            URL url = new URL(urlstr);
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            byte[] b = input.getBytes();
            httpConn.setRequestProperty("Content-Length", "" + b.length);
            httpConn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
            httpConn.setRequestMethod("POST");
            httpConn.setDoOutput(true);
            httpConn.setDoInput(true);
            myAuthenticator.setCredentials(username, password);
            OutputStream out = httpConn.getOutputStream();
            out.write(b);
            out.close();
            InputStreamReader isreader = new InputStreamReader(httpConn.getInputStream());
            BufferedReader in = new BufferedReader(isreader);
            StringBuilder result = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) result.append(inputLine);
            in.close();
            return result.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
