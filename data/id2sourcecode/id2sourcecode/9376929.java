    @Test
    public void testRedirect() throws Exception {
        dispatcher.getRegistry().addPerRequestResource(RedirectResource.class);
        {
            testRedirect(ProxyFactory.create(RedirectClient.class, generateBaseUrl()).get());
            testRedirect(createClientRequest("/redirect").get());
        }
        System.out.println("*****");
        {
            URL url = createURL("/redirect");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setInstanceFollowRedirects(false);
            conn.setRequestMethod("GET");
            for (Object name : conn.getHeaderFields().keySet()) {
                System.out.println(name);
            }
            System.out.println(conn.getResponseCode());
        }
    }
