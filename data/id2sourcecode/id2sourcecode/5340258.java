    public List fetchID(String query, int max) {
        List pubmed_ids = new ArrayList();
        try {
            String quoted_query = java.net.URLEncoder.encode(query, "UTF-8");
            BufferedReader in;
            String url = "http://eutils.ncbi.nlm.nih.gov/entrez/eutils/esearch.fcgi?db=pubmed&term=" + quoted_query + "&retmax=" + max;
            in = new BufferedReader(new InputStreamReader((new URL(url)).openStream()));
            String inputLine = "";
            while ((inputLine = in.readLine()) != null) {
                if (inputLine.startsWith("		<Id>")) {
                    StringTokenizer st = new StringTokenizer(inputLine, "<>/ ");
                    while (st.hasMoreTokens()) {
                        String tok = st.nextToken();
                        if (!(tok.startsWith("I"))) {
                            tok = tok.trim();
                            if (tok.length() > 0 && !pubmed_ids.contains(tok)) {
                                pubmed_ids.add(tok);
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return pubmed_ids;
    }
