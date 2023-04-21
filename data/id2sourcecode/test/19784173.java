    public static void main(String[] args) throws Exception {
        HttpGet get = new HttpGet("http://localhost/api/test.html");
        get.setHeader("X-WSSE", "UsernameToken Username=\"admin\", " + "PasswordDigest=\"9wPf4azc4MBhzCh5HlBU3S9fNdo=\", " + "Nonce=\"MTBjMGE1MTFlNTYwMWVkOQ==\", " + "Created=\"2009-07-18T14:40:53Z\"");
        DefaultHttpClient client = new DefaultHttpClient();
        HttpResponse response = client.execute(get);
        System.out.println(response.getStatusLine().getStatusCode());
    }
