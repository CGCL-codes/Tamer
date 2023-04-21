        private static String getCookie(String url) {
            String cookie = CookieManager.getInstance().getCookie(url);
            if (cookie == null || cookie.length() == 0) {
                Log.d(TAG, "Book Search cookie was missing or expired");
                HttpUriRequest head = new HttpHead(url);
                AndroidHttpClient client = AndroidHttpClient.newInstance(USER_AGENT);
                try {
                    HttpResponse response = client.execute(head);
                    if (response.getStatusLine().getStatusCode() == 200) {
                        Header[] cookies = response.getHeaders("set-cookie");
                        for (Header theCookie : cookies) {
                            CookieManager.getInstance().setCookie(url, theCookie.getValue());
                        }
                        CookieSyncManager.getInstance().sync();
                        cookie = CookieManager.getInstance().getCookie(url);
                    }
                } catch (IOException e) {
                    Log.w(TAG, "Error setting book search cookie", e);
                }
                client.close();
            }
            return cookie;
        }
