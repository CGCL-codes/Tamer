    public void testSearchOneSequence() {
        FASTAFileNodeHelper fastaHelper = new FASTAFileNodeHelper();
        RandomAccessFile fastaFile = null;
        try {
            File testFile = TestUtils.getTestFile("shared" + File.separator + "testfiles" + File.separator + "fasta" + File.separator + "test.nucleotide.fasta");
            String testAcc = "JCVI_ORF_1096130161890";
            fastaFile = new RandomAccessFile(testFile, "r");
            InFileChannelHandler fastaFileChannelHandler = new InFileChannelHandler(fastaFile.getChannel());
            FASTAFileNodeHelper.FASTASequenceCache fastaSequenceCache = new FASTAFileNodeHelper.FASTASequenceCache();
            BaseSequenceEntity seqEntity = fastaHelper.readSequence(fastaFileChannelHandler, testAcc, fastaSequenceCache);
            assertTrue(seqEntity != null && seqEntity instanceof ORF);
            assertTrue(seqEntity.getCameraAcc().equals(testAcc));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        } finally {
            if (fastaFile != null) {
                try {
                    fastaFile.close();
                } catch (Exception ignore) {
                }
            }
        }
    }
