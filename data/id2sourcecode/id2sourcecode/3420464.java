    public boolean executeTest() {
        boolean[] r = new boolean[numAttempts];
        int i = 0;
        boolean R = false;
        while (i < numAttempts) {
            r[i] = false;
            Exception e = null;
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
                        R = R | true;
                        r[i] = true;
                    }
                }
            } catch (IOException io) {
                e = io;
                io.printStackTrace();
            } finally {
                TestResult tr = new TestResult(r[i], statusLine, e);
                results.add(tr);
            }
            i++;
        }
        return R;
    }
