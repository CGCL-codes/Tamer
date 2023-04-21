    private BufferedReader openSearchForResultSize() throws IOException {
        String date_part = makeDateRestriction();
        String agricola_base_url = "http://agricola.nal.usda.gov/cgi-bin/Pwebrecon.cgi?BOOL1=any+of+these&" + "FLD1=Keyword+Anywhere+%28GKEY%29&GRP1=AND+with+next+set";
        String query_url = agricola_base_url + "&SAB1=" + searchfor + date_part + "&PID=" + sessionId;
        BufferedReader in = new BufferedReader(new InputStreamReader((new URL(query_url)).openStream()));
        return in;
    }
