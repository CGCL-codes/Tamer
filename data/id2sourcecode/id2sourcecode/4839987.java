    public boolean execute() {
        boolean r = false;
        try {
            DefaultHttpClient httpclient = new DefaultHttpClient();
            httpclient.getCredentialsProvider().setCredentials(new AuthScope(host, port), new UsernamePasswordCredentials(user, pwd));
            HttpGet httpget = new HttpGet(url);
            System.out.println("executing request:" + httpget.getRequestLine());
            HttpResponse response = httpclient.execute(httpget);
            HttpEntity entity = response.getEntity();
            statusLine = response.getStatusLine().toString();
            System.out.println("statusLine:" + statusLine);
            if (entity != null) {
                content = entity.getContent();
                isr = new InputStreamReader(content);
                lnr = new LineNumberReader(isr);
                answer = lnr.readLine();
                if (answer.startsWith("OK") && statusLine.trim().endsWith("OK")) {
                    r = true;
                }
            }
        } catch (IOException io) {
            io.printStackTrace();
        }
        return r;
    }
