    @Test
    public void testTempEval2010Writer() throws Exception {
        File writerDirectory = new File(this.outputDirectory, "writer");
        CollectionReader reader = TempEval2010CollectionReader.getCollectionReader(this.outputDirectory.getPath());
        AnalysisEngine annotator = AnalysisEngineFactory.createPrimitive(TempEval2010GoldAnnotator.getDescription(), TempEval2010GoldAnnotator.PARAM_TEXT_VIEWS, new String[] { CAS.NAME_DEFAULT_SOFA, "E2DCT", "E2SST", "E2SE", "ME2ME" }, TempEval2010GoldAnnotator.PARAM_DOCUMENT_CREATION_TIME_VIEWS, new String[] { CAS.NAME_DEFAULT_SOFA, "E2DCT" }, TempEval2010GoldAnnotator.PARAM_TIME_EXTENT_VIEWS, new String[] { CAS.NAME_DEFAULT_SOFA, "E2SST" }, TempEval2010GoldAnnotator.PARAM_TIME_ATTRIBUTE_VIEWS, new String[] { CAS.NAME_DEFAULT_SOFA, "E2SST" }, TempEval2010GoldAnnotator.PARAM_EVENT_EXTENT_VIEWS, new String[] { CAS.NAME_DEFAULT_SOFA, "E2DCT", "E2SST", "E2SE", "ME2ME" }, TempEval2010GoldAnnotator.PARAM_EVENT_ATTRIBUTE_VIEWS, new String[] { CAS.NAME_DEFAULT_SOFA, "E2DCT", "E2SST", "E2SE", "ME2ME" }, TempEval2010GoldAnnotator.PARAM_TEMPORAL_LINK_EVENT_TO_DOCUMENT_CREATION_TIME_VIEWS, new String[] { "E2DCT" }, TempEval2010GoldAnnotator.PARAM_TEMPORAL_LINK_EVENT_TO_SAME_SENTENCE_TIME_VIEWS, new String[] { "E2SST" }, TempEval2010GoldAnnotator.PARAM_TEMPORAL_LINK_EVENT_TO_SUBORDINATED_EVENT_VIEWS, new String[] { "E2SE" }, TempEval2010GoldAnnotator.PARAM_TEMPORAL_LINK_MAIN_EVENT_TO_NEXT_SENTENCE_MAIN_EVENT_VIEWS, new String[] { "ME2ME" });
        AnalysisEngine writer = AnalysisEngineFactory.createPrimitive(TempEval2010Writer.getDescription(), TempEval2010Writer.PARAM_OUTPUT_DIRECTORY, writerDirectory.getPath(), TempEval2010Writer.PARAM_TEXT_VIEW, CAS.NAME_DEFAULT_SOFA, TempEval2010Writer.PARAM_DOCUMENT_CREATION_TIME_VIEW, CAS.NAME_DEFAULT_SOFA, TempEval2010Writer.PARAM_TIME_EXTENT_VIEW, CAS.NAME_DEFAULT_SOFA, TempEval2010Writer.PARAM_TIME_ATTRIBUTE_VIEW, CAS.NAME_DEFAULT_SOFA, TempEval2010Writer.PARAM_EVENT_EXTENT_VIEW, CAS.NAME_DEFAULT_SOFA, TempEval2010Writer.PARAM_EVENT_ATTRIBUTE_VIEW, CAS.NAME_DEFAULT_SOFA, TempEval2010Writer.PARAM_TEMPORAL_LINK_EVENT_TO_DOCUMENT_CREATION_TIME_VIEW, "E2DCT", TempEval2010Writer.PARAM_TEMPORAL_LINK_EVENT_TO_SAME_SENTENCE_TIME_VIEW, "E2SST", TempEval2010Writer.PARAM_TEMPORAL_LINK_EVENT_TO_SUBORDINATED_EVENT_VIEW, "E2SE", TempEval2010Writer.PARAM_TEMPORAL_LINK_MAIN_EVENT_TO_NEXT_SENTENCE_MAIN_EVENT_VIEW, "ME2ME");
        SimplePipeline.runPipeline(reader, annotator, writer);
        this.assertFileText("base-segmentation.tab", "data", "writer");
        this.assertFileText("timex-extents.tab", "key", "writer");
        this.assertFileText("timex-attributes.tab", "key", "writer");
        this.assertFileText("event-extents.tab", "key", "writer");
        this.assertFileText("event-attributes.tab", "key", "writer");
        this.assertFileText("tlinks-dct-event.tab", "key", "writer");
        this.assertFileText("tlinks-timex-event.tab", "key", "writer");
        this.assertFileText("tlinks-subordinated-events.tab", "key", "writer");
        this.assertFileText("tlinks-main-events.tab", "key", "writer");
        File dctInput = new File(this.outputDirectory, "dct-en.txt");
        File dctOutput = new File(this.outputDirectory, "writer/dct.txt");
        this.assertFileText("dct.txt", dctInput, dctOutput);
    }
