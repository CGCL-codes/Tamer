    @Test
    public void test3() throws Exception {
        AnalysisEngine engine = AnalysisEngineFactory.createPrimitive(LineWriter.class, typeSystemDescription, LineWriter.PARAM_OUTPUT_FILE_NAME, new File(outputDirectory, "output.txt").getPath(), LineWriter.PARAM_OUTPUT_ANNOTATION_CLASS_NAME, Sentence.class.getName(), LineWriter.PARAM_BLOCK_ANNOTATION_CLASS_NAME, "org.apache.uima.jcas.tcas.DocumentAnnotation", LineWriter.PARAM_BLOCK_WRITER_CLASS_NAME, DoNothingBlockWriter.class.getName(), LineWriter.PARAM_ANNOTATION_WRITER_CLASS_NAME, CoveredTextAnnotationWriter.class.getName());
        String text = "If you like line writer, then you should really check out line rider.";
        tokenBuilder.buildTokens(jCas, text);
        engine.process(jCas);
        Token token = JCasUtil.selectByIndex(jCas, Token.class, 0);
        Assert.assertEquals("If", token.getCoveredText());
        jCas.reset();
        text = "I highly recommend reading 'Three Cups of Tea' by Greg Mortenson.\n Swashbuckling action and inspirational story.";
        tokenBuilder.buildTokens(jCas, text);
        engine.process(jCas);
        DocumentAnnotation da = JCasUtil.selectByIndex(jCas, DocumentAnnotation.class, 0);
        assertNotNull(da);
        engine.collectionProcessComplete();
        String expectedText = "If you like line writer, then you should really check out line rider." + newline + "I highly recommend reading 'Three Cups of Tea' by Greg Mortenson." + newline + "Swashbuckling action and inspirational story." + newline;
        File outputFile = new File(this.outputDirectory, "output.txt");
        assertTrue(outputFile.exists());
        String actualText = FileUtils.file2String(outputFile);
        Assert.assertEquals(expectedText, actualText);
    }
