    public void testLectureM3_2_c9() {
        final CtuluLog log = new CtuluLog();
        final CrueIOResu<CrueData> readModele = ReadHelper.readModele(log, "/Etu3-2/M3-2_c9.dc", "/Etu3-2/M3-2_c9.dh");
        final File createTempFile = createTempFile();
        ReadHelper.writeModeleCrue9(log, createTempFile, readModele.getMetier());
        testAnalyser(log);
    }
