    @Override
    public void run() throws Exception {
        ConverterXMLReader reader = new ConverterXMLReader(params.getParamFile().getAbsolutePath());
        PrintStream out = reader.getPrintOutputDevice();
        Date dateNow = new Date();
        out.println("************************************************************************");
        out.println("*                                                                      *");
        out.println("*                         JPLSpectrumConverter                         *");
        out.println("*               MS/MS spectrum filtering and editing tool              *");
        out.println("*                              Version 1.0                             *");
        out.println("*                                                                      *");
        out.println("************************************************************************");
        out.println();
        out.println("Start time: " + dateNow.toString());
        out.println();
        String directory = reader.getInputSpectrumFileDirectory();
        String regex = reader.getInputSpectrumFileRegExp();
        SpectrumProcessorManager spectrumTransformer = reader.getSpectrumFilterManager();
        MSDispatcher dispatcher = new MSDispatcher(directory, regex);
        dispatcher.setSpectrumTransformer(spectrumTransformer);
        dispatcher.setMaxNrOfSpectra(reader.getMaximalNrOfSpectraPerBatch());
        boolean combineSpectra = reader.getCombineFilesFlag();
        if (reader.calibrateSpectra) combineSpectra = false;
        dispatcher.setCombineFilesFlag(combineSpectra);
        dispatcher.setSpectrumQualityFilter(reader.getSpectrumQualityFilter());
        dispatcher.setPrecursorCondition(reader.getPrecursorCondition());
        dispatcher.setMSLevelCondition(reader.getMSLevelCondition());
        if (reader.getRemoveIdentifiedSpectraFlag()) {
            System.out.println("Remove Spectra from " + reader.getRemovePepXMLFile().getAbsolutePath());
            Set<String> specIdents = readPSM(reader.getRemovePepXMLFile(), reader.getRemoveProbScoreThreshold());
            dispatcher.setTitleCondition(removeTitleFilter(specIdents));
        }
        if (reader.getRetainIdentifiedSpectraFlag()) {
            System.out.println("Retain Spectra from " + reader.getRemovePepXMLFile().getAbsolutePath());
            Set<String> specIdents = readPSM(reader.getRetainPepXMLFile(), reader.getRetainProbScoreThreshold());
            dispatcher.setTitleCondition(retainTitleFilter(specIdents));
        }
        dispatcher.buildSpectrumCondition();
        int cnt = 1;
        int totCnt = 0;
        if (reader.getWriteMGFFileFlag()) {
            ArrayList<MSScan> spectrumBatch = new ArrayList<MSScan>();
            while (dispatcher.getNextSpectrumBatch(spectrumBatch)) {
                totCnt += spectrumBatch.size();
                out.println();
                out.println("Batch " + cnt + " (" + spectrumBatch.size() + " spectra)" + " of file " + dispatcher.getProcessedFile().getAbsolutePath() + " is being processed.");
                String batchStr = "";
                if (!dispatcher.getReadInOneBatchFlag()) {
                    batchStr = "Batch" + cnt + "_";
                }
                if (reader.getCalibrateSpectraFlag()) {
                    System.out.println("Calibrate Spectra from " + dispatcher.getProcessedFile().getAbsolutePath());
                    Map<String, ArrayList<Double>> mzMap = readPSM4Calib(reader.getCalibratePepXMLFile(), dispatcher.getProcessedFile(), reader.getCalibrateProbScoreThreshold(), reader.getCalibratePrecError());
                    FileWriter mzFile = null;
                    try {
                        String name = dispatcher.getProcessedFile().getName();
                        name = name.replace(".mgf", "_calib.txt");
                        mzFile = new FileWriter(reader.getMGFDir() + name, false);
                        mzFile.write("measuredMz,theorMz,calibMz\n");
                    } catch (Exception e) {
                    }
                    ArrayList<Double> refMzs = mzMap.get("reference");
                    ArrayList<Double> measMzs = mzMap.get("measured");
                    StraightLineFitter fitter = new StraightLineFitter(measMzs, refMzs);
                    CALIB_SLOPE = fitter.getFitSlope();
                    CALIB_OFFSET = fitter.getFitOffset();
                    for (int i = 0; i < refMzs.size(); i++) {
                        double mz = measMzs.get(i);
                        mz = CALIB_OFFSET + CALIB_SLOPE * mz;
                        mzFile.write(measMzs.get(i).toString() + "," + refMzs.get(i).toString() + "," + mz + "\n");
                        mzFile.flush();
                    }
                    mzFile.close();
                    System.out.println("mz = " + CALIB_OFFSET + " + mz*" + CALIB_SLOPE);
                }
                writeSpectra2Mgf(spectrumBatch, batchStr, reader, dispatcher, false);
                spectrumBatch.clear();
                cnt++;
            }
            out.println();
            if (!dispatcher.getReadInOneBatchFlag()) out.println(totCnt + " spectra were processed.");
            dispatcher.reset();
        }
        if (reader.getCreateMS2SimMatrixFlag()) {
            createMS2SimMatrix(dispatcher, reader, out);
        }
        out.println();
        dateNow = new Date();
        out.println("End time: " + dateNow.toString());
    }
