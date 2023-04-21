    public void testEcriture(String file, CoeurConfigContrat version) {
        final CtuluLog analyzer = new CtuluLog();
        final File f = createTempFile();
        Crue10FileFormat<Object> fileFormat = Crue10FileFormatFactory.getVersion(CrueFileType.OPTG, version);
        final boolean res = fileFormat.writeMetierDirect(readModele(file, version), f, analyzer, TestCrueConfigMetierLoaderDefault.DEFAULT);
        assertFalse(analyzer.containsErrors());
        analyzer.clear();
        boolean valide = fileFormat.isValide(f, analyzer);
        testAnalyser(analyzer);
        assertTrue(valide);
        final OrdPrtGeoModeleBase read = (OrdPrtGeoModeleBase) fileFormat.read(f, analyzer, createDefault()).getMetier();
        testDataModele3(read);
    }
