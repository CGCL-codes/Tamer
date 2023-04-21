        private String getCookie(String url) {
            String cookie = CookieManager.getInstance().getCookie(url);
            if (cookie == null || cookie.length() == 0) {
                Log.v(TAG, "Book Search cookie was missing or expired");
                HttpUriRequest head = new HttpHead(url);
                AndroidHttpClient client = AndroidHttpClient.newInstance(USER_AGENT);
                try {
                    HttpResponse response = client.execute(head);
                    if (response.getStatusLine().getStatusCode() == 200) {
                        Header[] cookies = response.getHeaders("set-cookie");
                        for (int x = 0; x < cookies.length; x++) {
                            CookieManager.getInstance().setCookie(url, cookies[x].getValue());
                        }
                        CookieSyncManager.getInstance().sync();
                        cookie = CookieManager.getInstance().getCookie(url);
                    }
                } catch (IOException e) {
                    Log.e(TAG, e.toString());
                }
                client.close();
            }
            return cookie;
        }
