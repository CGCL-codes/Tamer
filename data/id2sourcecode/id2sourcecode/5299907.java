    public static void main(String[] args) throws Exception {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        HttpContext localContext = new BasicHttpContext();
        HttpGet httpget = new HttpGet("http://localhost/test");
        boolean trying = true;
        while (trying) {
            System.out.println("executing request " + httpget.getRequestLine());
            HttpResponse response = httpclient.execute(httpget, localContext);
            System.out.println("----------------------------------------");
            System.out.println(response.getStatusLine());
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                entity.consumeContent();
            }
            int sc = response.getStatusLine().getStatusCode();
            AuthState authState = null;
            if (sc == HttpStatus.SC_UNAUTHORIZED) {
                authState = (AuthState) localContext.getAttribute(ClientContext.TARGET_AUTH_STATE);
            }
            if (sc == HttpStatus.SC_PROXY_AUTHENTICATION_REQUIRED) {
                authState = (AuthState) localContext.getAttribute(ClientContext.PROXY_AUTH_STATE);
            }
            if (authState != null) {
                System.out.println("----------------------------------------");
                AuthScope authScope = authState.getAuthScope();
                System.out.println("Please provide credentials");
                System.out.println(" Host: " + authScope.getHost() + ":" + authScope.getPort());
                System.out.println(" Realm: " + authScope.getRealm());
                BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
                System.out.print("Enter username: ");
                String user = console.readLine();
                System.out.print("Enter password: ");
                String password = console.readLine();
                if (user != null && user.length() > 0) {
                    Credentials creds = new UsernamePasswordCredentials(user, password);
                    httpclient.getCredentialsProvider().setCredentials(authScope, creds);
                    trying = true;
                } else {
                    trying = false;
                }
            } else {
                trying = false;
            }
        }
    }
