    public static List<BibtexEntry> fetchMedline(String id, OutputPrinter status) {
        String baseUrl = "http://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi?db=pubmed&retmode=xml&rettype=citation&id=" + id;
        try {
            URL url = new URL(baseUrl);
            URLConnection data = url.openConnection();
            return new MedlineImporter().importEntries(data.getInputStream(), status);
        } catch (IOException e) {
            return new ArrayList<BibtexEntry>();
        }
    }
