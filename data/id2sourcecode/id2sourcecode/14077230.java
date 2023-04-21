    public static void main(String... args) throws Exception {
        HttpClient client = new DefaultHttpClient();
        HttpHost proxy = new HttpHost("proxy.houston.hp.com", 8080);
        client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
        System.out.println("AquaBrowser systems, expect true");
        for (String libUrl : libUrls) {
            URI uri = new URI(libUrl);
            URI mangledUri = new URI(uri.getScheme() + "://" + uri.getAuthority() + "/rss.ashx");
            HttpGet get = new HttpGet(mangledUri);
            try {
                HttpResponse resp = client.execute(get);
                StatusLine respStatus = resp.getStatusLine();
                String retStatus = "";
                if (respStatus.getStatusCode() == 200) {
                    retStatus = "Supported";
                } else if (respStatus.getStatusCode() == 503 && respStatus.getReasonPhrase().equals("Rss module not active.")) {
                    retStatus = "AquaBrowser, but unsupported";
                } else {
                    retStatus = "Not AquaBrowser";
                }
                System.out.println("Status: " + retStatus);
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (get != null) {
                    get.abort();
                }
            }
        }
        System.out.println("Other systems, expect false");
        for (String libUrl : badUrls) {
            URI uri = new URI(libUrl);
            URI mangledUri = new URI(uri.getScheme() + "://" + uri.getAuthority() + "/rss.ashx");
            HttpGet get = new HttpGet(mangledUri);
            try {
                HttpResponse resp = client.execute(get);
                System.out.println("Status: " + resp.getStatusLine());
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (get != null) {
                    get.abort();
                }
            }
        }
    }
