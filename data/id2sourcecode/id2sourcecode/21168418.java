    public void housekeepWiretapEvents() {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        Credentials defaultcreds = new UsernamePasswordCredentials(this.wiretapEventHousekeepingUserName, this.wiretapEventHousekeepingPassword);
        httpclient.getCredentialsProvider().setCredentials(AuthScope.ANY, defaultcreds);
        BasicHttpContext localcontext = new BasicHttpContext();
        BasicScheme basicAuth = new BasicScheme();
        localcontext.setAttribute("preemptive-auth", basicAuth);
        httpclient.addRequestInterceptor(new PreemptiveAuth(), 0);
        HttpPost httppost = new HttpPost(this.wiretapEventHousekeepingUrl);
        try {
            HttpResponse response = httpclient.execute(httppost, localcontext);
            int statusCode = response.getStatusLine().getStatusCode();
            if (!(statusCode == HttpStatus.SC_MOVED_TEMPORARILY || statusCode == HttpStatus.SC_OK)) {
                logger.error("Call failed, Status Code = [" + statusCode + "]");
                throw new HttpException();
            }
            logger.info("housekeepWiretapEvents was called successfully.");
        } catch (IOException e) {
            logger.error("Call to housekeep failed.", e);
        } catch (HttpException e) {
            logger.error("Call to housekeep failed.", e);
        } finally {
            httpclient.getConnectionManager().shutdown();
        }
    }
