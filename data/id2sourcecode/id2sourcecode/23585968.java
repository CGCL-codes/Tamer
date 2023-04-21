    public void testSoaEchoSink() throws IOException, InterruptedException {
        URL url = new URL("http://localhost:" + PORT + "/lightsoa/echoservice/sinking?msg=samurai");
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        String result = in.readLine();
        in.close();
        assertTrue(result.contains("samurai"));
    }
