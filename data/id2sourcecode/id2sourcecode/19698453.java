    @Test
    public void testDigest() {
        trypsin.digest(sequence);
        regexTrypsin.digest(sequence);
        List<JPLIAASequence> digests = trypsin.getDigests();
        List<JPLIAASequence> digests2 = regexTrypsin.getDigests();
        logger.info(digests);
        assertEquals(digests.size(), digests2.size(), 0);
        assertEquals(digests.size(), 4, 0);
    }
