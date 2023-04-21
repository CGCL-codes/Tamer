    @Test
    public void testDigestionWithMissedCleavages() {
        digester.setNumberOfMissedCleavage(2);
        digester.digest(sequence);
        final Set<DigestedPeptide> digestSet = digester.getDigests();
        System.out.println(digestSet);
        assertEquals(8, digestSet.size());
    }
