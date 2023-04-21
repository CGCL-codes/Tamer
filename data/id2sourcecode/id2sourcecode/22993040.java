    public void testSoaEchoUrl() throws IOException, InterruptedException {
        URL url = new URL("http://localhost:" + PORT + "/lightsoa/asset/" + PK + "/play?movie=" + TestLocalSoaAssets.MOVIEKEY.toUrlEncodedString());
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        String result = in.readLine();
        in.close();
        assertTrue(result.contains(TestLocalSoaAssets.MOVIEKEY.getId()));
    }
