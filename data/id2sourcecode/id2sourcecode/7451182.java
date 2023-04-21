    @Test
    public void testCustomerResource() throws Exception {
        System.out.println("*** Create a new Customer ***");
        String newCustomer = "<customer>" + "<first-name>Bill</first-name>" + "<last-name>Burke</last-name>" + "<street>256 Clarendon Street</street>" + "<city>Boston</city>" + "<state>MA</state>" + "<zip>02115</zip>" + "<country>USA</country>" + "</customer>";
        DefaultHttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost("http://localhost:9095/customers");
        StringEntity entity = new StringEntity(newCustomer);
        entity.setContentType("application/xml");
        post.setEntity(entity);
        HttpClientParams.setRedirecting(post.getParams(), false);
        HttpResponse response = client.execute(post);
        Assert.assertEquals(201, response.getStatusLine().getStatusCode());
        System.out.println("Location: " + response.getLastHeader("Location"));
        HttpPatch patch = new HttpPatch("http://localhost:9095/customers/1");
        String patchCustomer = "<customer>" + "<first-name>William</first-name>" + "</customer>";
        entity = new StringEntity(patchCustomer);
        entity.setContentType("application/xml");
        patch.setEntity(entity);
        response = client.execute(patch);
        Assert.assertEquals(204, response.getStatusLine().getStatusCode());
        System.out.println("**** After Update ***");
        HttpGet get = new HttpGet("http://localhost:9095/customers/1");
        response = client.execute(get);
        Assert.assertEquals(200, response.getStatusLine().getStatusCode());
        System.out.println("Content-Type: " + response.getEntity().getContentType());
        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        String line = reader.readLine();
        while (line != null) {
            System.out.println(line);
            line = reader.readLine();
        }
    }
