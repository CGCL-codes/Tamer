    public static InputStream openEFetchStream(Set<RsId> rsids) throws IOException {
        StringBuilder sb = new StringBuilder("db=snp&retmode=xml");
        for (RsId rs : rsids) {
            sb.append("&id=" + rs.getId());
        }
        URL url = new URL("http://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi");
        URLConnection con = url.openConnection();
        con.setDoOutput(true);
        OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
        wr.write(sb.toString());
        wr.flush();
        wr.close();
        return con.getInputStream();
    }
