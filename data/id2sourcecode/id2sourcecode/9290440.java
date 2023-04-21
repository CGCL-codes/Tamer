    public void insertSampleData() throws Exception {
        URL urlToImport = new URL("ftp://ftp.ebi.ac.uk/pub/databases/intact/current/psi25/pmid/2007/10094392.xml");
        URL urlToImport2 = new URL("ftp://ftp.ebi.ac.uk/pub/databases/intact/current/psi25/pmid/2007/10220404.xml");
        PsiExchange.importIntoIntact(urlToImport.openStream());
        PsiExchange.importIntoIntact(urlToImport2.openStream());
    }
