    @Test
    public void testDigestionWithMissedCleavages() throws JPLEmptySequenceException {
        trypsin.setNumberOfMissedCleavage(2);
        trypsin.digest(sequence);
        regexTrypsin.setNumberOfMissedCleavage(2);
        regexTrypsin.digest(sequence);
        List<JPLIAASequence> digests = trypsin.getDigests();
        Set<JPLIAASequence> digestSet = trypsin.getUniqueDigests();
        Set<JPLIAASequence> digests2 = regexTrypsin.getUniqueDigests();
        logger.info(digests);
        assertEquals(digestSet.size(), digests2.size(), 0);
        assertEquals(digestSet.size(), 8, 0);
    }
