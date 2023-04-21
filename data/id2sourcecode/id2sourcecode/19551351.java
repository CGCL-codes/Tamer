    public OrdPrtReseau doEcriture(String file, CoeurConfigContrat version) {
        final CtuluLog analyzer = new CtuluLog();
        final File f = createTempFile();
        Crue10FileFormat<Object> fileFormat = Crue10FileFormatFactory.getVersion(CrueFileType.OPTR, version);
        fileFormat.writeMetierDirect(readModele(file, version), f, analyzer, TestCrueConfigMetierLoaderDefault.DEFAULT);
        assertFalse(analyzer.containsErrors());
        analyzer.clear();
        boolean valide = fileFormat.isValide(f, analyzer);
        testAnalyser(analyzer);
        assertTrue(valide);
        return (OrdPrtReseau) fileFormat.read(f, analyzer, createDefault()).getMetier();
    }
