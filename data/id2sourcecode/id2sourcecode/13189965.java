    @Ignore
    @Test
    public void testGetHouthalenLocations() throws Exception {
        final Map variables = new HashMap();
        variables.put("prefix", "houthalen");
        variables.put("language", "nl");
        final StringWriter writer = new StringWriter();
        final VelocityContext context = new VelocityContext(variables);
        locationsListTemplate.merge(context, writer);
        final String request = writer.toString();
        final URLConnection urlConnection = LOCATION_URL.openConnection();
        urlConnection.setUseCaches(false);
        urlConnection.setDoOutput(true);
        urlConnection.setRequestProperty("accept-charset", "UTF-8");
        urlConnection.setRequestProperty("content-type", "text/xml; charset=utf-8");
        urlConnection.setRequestProperty("Content-Length", "" + request.length());
        urlConnection.setRequestProperty("SOAPAction", "http://myRoute.be/GetLocationList");
        OutputStreamWriter outputWriter = new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8");
        outputWriter.write(request);
        outputWriter.flush();
        final InputStream result = urlConnection.getInputStream();
        final String response = IOUtils.toString(result);
        System.out.println(response);
        assertTrue(response.contains("<GeoId>"));
    }
