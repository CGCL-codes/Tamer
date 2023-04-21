    @Test
    public void testGetTravelTime() throws Exception {
        final String houthalenDisplayId = "3065";
        final String houthalenGeoId = "996";
        final String antwerpenDisplayId = "1882";
        final String antwerpenGeoId = "438";
        final Map variables = new HashMap();
        variables.put("fromId", "1");
        variables.put("toId", "2");
        final StringWriter writer = new StringWriter();
        final VelocityContext context = new VelocityContext(variables);
        travelTimesTemplate.merge(context, writer);
        final String request = writer.toString();
        final URLConnection urlConnection = TRAVEL_URL.openConnection();
        urlConnection.setUseCaches(false);
        urlConnection.setDoOutput(true);
        urlConnection.setRequestProperty("accept-charset", "UTF-8");
        urlConnection.setRequestProperty("content-type", "application/soap+xml; charset=utf-8");
        urlConnection.setRequestProperty("Content-Length", "" + request.length());
        OutputStreamWriter outputWriter = new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8");
        outputWriter.write(request);
        outputWriter.flush();
        try {
            final InputStream result = urlConnection.getInputStream();
            final String response = IOUtils.toString(result);
            System.out.println(response);
        } catch (IOException iOException) {
            System.out.println("Something happened: " + iOException);
        }
    }
