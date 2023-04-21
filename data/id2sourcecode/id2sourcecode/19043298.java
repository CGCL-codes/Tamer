    @Override
    public boolean isCompatible(String url) throws URISyntaxException {
        URI checkurl = new URI(url);
        HttpGet get = null;
        try {
            HttpClient client = new DefaultHttpClient();
            get = new HttpGet(checkurl);
            HttpResponse resp = client.execute(get);
            Scanner s = new Scanner(resp.getEntity().getContent());
            String pattern = s.findWithinHorizon("<link.*\"/scripts/ProStyles.css\"", 0);
            return (pattern != null && !pattern.equals(""));
        } catch (ClientProtocolException e) {
            Log.e(this.getClass().getName(), "failed checking compatibility", e);
        } catch (IOException e) {
            Log.e(this.getClass().getName(), "failed checking compatibility", e);
        } finally {
            if (get != null) {
                get.abort();
            }
        }
        return false;
    }
