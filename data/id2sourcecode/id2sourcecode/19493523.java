    @Test(groups = { "jezuch.utils" }, dependsOnGroups = { "jezuch.utils.init" }, expectedExceptions = ParseException.class, dataProvider = "invalid-inis")
    public void invalidIni(URL url) throws IOException, ParseException {
        Reader reader = new InputStreamReader(url.openStream());
        try {
            new INIFile(reader);
        } finally {
            reader.close();
        }
    }
