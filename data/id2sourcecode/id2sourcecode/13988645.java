    @Test
    public void testCodes() throws IOException {
        URL url = new URL("http://rest.bioontology.org/bioportal/virtual/children/1070/GO:0043227?email=example@example.org");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        log.info(conn.getResponseCode());
        log.info(conn.getResponseMessage());
    }
