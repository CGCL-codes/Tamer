    @Test
    public void testCustomPeptidase() {
        String sequenceString = "HKRDMHSNKR";
        Peptide sequence = new Peptide.Builder(sequenceString).build();
        CleavageSiteCutter siteCutter = new CleavageSiteCutter.Builder("X|[DN] or [DN]|X").build();
        Peptidase yass = Peptidase.getInstance("custom", siteCutter);
        digester = Digester.newInstance(yass);
        digester.digest(sequence);
        Set<DigestedPeptide> digests = digester.getDigests();
        Assert.assertEquals(5, digests.size());
        digester.setNumberOfMissedCleavage(1);
        digester.digest(sequence);
        digests = digester.getDigests();
        Assert.assertEquals(9, digests.size());
        digester.setNumberOfMissedCleavage(2);
        digester.digest(sequence);
        digests = digester.getDigests();
        Assert.assertEquals(12, digests.size());
        Iterator<Integer> iter = siteCutter.iterator(sequence);
        List<Integer> indices = new ArrayList<Integer>();
        while (iter.hasNext()) {
            indices.add(iter.next());
        }
        Assert.assertEquals(Arrays.asList(3, 4, 7, 8), indices);
    }
