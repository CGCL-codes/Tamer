    @Test
    public void testDeudaTotal() throws Exception {
        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet("http://localhost:8080/bn/rest/multas/");
        HttpResponse response = httpclient.execute(httpget);
        HttpEntity entity = response.getEntity();
        BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()));
        String cadena = reader.readLine();
        System.out.println("Invocando.. bn/rest/multas/: " + cadena);
        Assert.assertNotNull(cadena);
    }
