    @Test
    public void testTempEval2010WriterPartial() throws Exception {
        File writerDirectory = new File(this.outputDirectory, "writer");
        CollectionReader reader = TempEval2010CollectionReader.getCollectionReader(this.outputDirectory.getPath());
        AnalysisEngine annotator = AnalysisEngineFactory.createPrimitive(TempEval2010GoldAnnotator.getDescription());
        AnalysisEngine writer = AnalysisEngineFactory.createPrimitive(TempEval2010Writer.getDescription(), TempEval2010Writer.PARAM_OUTPUT_DIRECTORY, writerDirectory.getPath(), TempEval2010Writer.PARAM_TEXT_VIEW, CAS.NAME_DEFAULT_SOFA, TempEval2010Writer.PARAM_TIME_EXTENT_VIEW, CAS.NAME_DEFAULT_SOFA, TempEval2010Writer.PARAM_EVENT_ATTRIBUTE_VIEW, CAS.NAME_DEFAULT_SOFA);
        SimplePipeline.runPipeline(reader, annotator, writer);
        this.assertFileText("base-segmentation.tab", "data", "writer");
        this.assertFileText("timex-extents.tab", "key", "writer");
        this.assertFileMissing("timex-attributes.tab", "writer");
        this.assertFileMissing("event-extents.tab", "writer");
        this.assertFileText("event-attributes.tab", "key", "writer");
        this.assertFileMissing("tlinks-dct-event.tab", "writer");
        this.assertFileMissing("tlinks-timex-event.tab", "writer");
        this.assertFileMissing("tlinks-subordinated-events.tab", "writer");
        this.assertFileMissing("tlinks-main-events.tab", "writer");
        this.assertFileMissing("dct.txt", "writer");
    }
