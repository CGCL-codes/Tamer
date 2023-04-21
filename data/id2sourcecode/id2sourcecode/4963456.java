    public void testEcriture(String file, CoeurConfigContrat version) {
        final CtuluLog analyzer = new CtuluLog();
        final File f = createTempFile();
        final boolean res = Crue10FileFormatFactory.getInstance().getFileFormat(CrueFileType.OPTI, version).writeMetierDirect(read(file, version), f, analyzer, version.getCrueConfigMetier());
        testAnalyser(analyzer);
        assertFalse(analyzer.containsErrors());
        final OrdPrtCIniModeleBase read = (OrdPrtCIniModeleBase) Crue10FileFormatFactory.getInstance().getFileFormat(CrueFileType.OPTI, version).read(f, analyzer, createDefault()).getMetier();
        testDataModele3(read);
        if (version.getXsdVersion().equals(Crue10FileFormatFactory.V_1_1_1)) {
            testSortiesTrueAndInfo(read);
        } else {
            testSortiesFalseAndDebug(read);
        }
    }
