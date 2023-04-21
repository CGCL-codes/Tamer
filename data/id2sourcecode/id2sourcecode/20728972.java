    public void fetch(Set<Integer> pmids) throws SAXException, IOException {
        URL url = new URL("http://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi");
        URLConnection connection = url.openConnection();
        connection.setDoOutput(true);
        OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
        out.write("db=pubmed" + "&rettype=full" + "&tool=meshfrequencies" + "&email=plindenbaum_at_yahoo.fr" + "&retmode=xml");
        for (Integer id : pmids) out.write("&id=" + id);
        out.close();
        InputStream in = connection.getInputStream();
        SAXParser parser = newSAXParser();
        parser.parse(in, new EFetchHandler());
        in.close();
    }
