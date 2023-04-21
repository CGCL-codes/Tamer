    @Test
    public void testDigestionWithMissedCleavages1() throws ParseException {
        Peptide peptide = new Peptide.Builder("QQDDFGKSVTDCTSNFCLFQSNSK").build();
        EditionRule cysCAM = EditionRule.newInstance("Cys_CAM", AAMotifMatcher.newInstance("C"), EditionAction.newFixedModifAction(ModificationFactory.valueOf(ChemicalFacade.getMolecule("C2H3NO"))));
        PeptideEditorFactory factory = PeptideEditorFactory.newInstance(cysCAM);
        Peptide digest = factory.transform(peptide).iterator().next();
        digester.setNumberOfMissedCleavage(1);
        digester.digest(digest);
        final Set<DigestedPeptide> digestSet = digester.getDigests();
        System.out.println(digestSet);
        for (DigestedPeptide digestedPept : digestSet) {
            if (digestedPept.getMC() == 1) {
                Assert.assertEquals("QQDDFGKSVTDC(C2H3NO)TSNFC(C2H3NO)LFQSNSK", digestedPept.toAAString());
            }
        }
    }
