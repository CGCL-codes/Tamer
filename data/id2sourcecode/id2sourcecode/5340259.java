    public List fetchFile(List ids) {
        BufferedReader in;
        List documents = new ArrayList();
        String url = "http://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi?db=pubmed&id=" + join(",", ids) + "&retmode=MEDLINE&rettype=MEDLINE";
        StringBuffer buffer = new StringBuffer();
        try {
            in = new BufferedReader(new InputStreamReader((new URL(url)).openStream()));
            String inputLine = "";
            while ((inputLine = in.readLine()) != null) {
                if (inputLine.equals("")) {
                    documents.add(buffer.toString());
                    buffer = new StringBuffer();
                } else {
                    buffer.append(inputLine);
                    buffer.append("\n");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return documents;
    }
