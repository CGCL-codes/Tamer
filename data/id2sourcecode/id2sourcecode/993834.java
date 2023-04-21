    public void execute() throws ExecutionException {
        System.out.println("\n\n------------ new session starts ------------\n");
        if (corpus != null) {
            if (corpus.size() != 0) if (corpus.indexOf(document) > 0) return;
        }
        boolean isUpdateFeatList = true;
        boolean isTraining = false;
        File wdResults = null;
        File logFile = null;
        int verbosityLogService = 1;
        BufferedWriter outFeatureVectors = null;
        isTraining = false;
        if (this.learningMode.equals(this.learningModeTraining)) isTraining = true; else isTraining = false;
        isUpdateFeatList = isTraining;
        if (isTraining) {
            System.out.println("Learning a new model from the segmented text...");
            System.out.println("Learning algorithm used is " + this.learningAlg);
            System.out.println("the model files will be stored in " + modelURL.toString());
            System.out.println("the text used for learning are in " + this.textFilesURL.toExternalForm());
        } else {
            System.out.println("Applying the learned model to segment Chinese text...");
            System.out.println("Learning algorithm used is " + this.learningAlg);
            System.out.println("the model files used are stored in " + modelURL.toString());
            System.out.println("the text for segmenting are in " + this.textFilesURL.toExternalForm());
        }
        verbosityLogService = 1;
        wdResults = Files.fileFromURL(modelURL);
        if (!wdResults.exists()) wdResults.mkdir();
        logFile = new File(wdResults, ConstantParameters.FILENAMEOFLOGFILE);
        try {
            LogService.init(logFile, true, verbosityLogService);
            NLPFeaturesList featuresList = null;
            featuresList = new NLPFeaturesList();
            featuresList.loadFromFile(wdResults, ConstantParameters.FILENAME_TERMS, this.textCode);
            if (!featuresList.featuresList.containsKey(ConstantParameters.NONFEATURE)) {
                int size = featuresList.featuresList.size() + 1;
                featuresList.featuresList.put(ConstantParameters.NONFEATURE, new Integer(size));
                featuresList.idfFeatures.put(ConstantParameters.NONFEATURE, new Integer(1));
            }
            Label2Id labelsAndId;
            labelsAndId = new Label2Id();
            labelsAndId.loadLabelAndIdFromFile(wdResults, ConstantParameters.FILENAMEOFLabelList);
            ExtensionFileFilter fileFilter = null;
            File[] xmlFiles = Files.fileFromURL(this.textFilesURL).listFiles(fileFilter);
            Arrays.sort(xmlFiles, new Comparator<File>() {

                public int compare(File a, File b) {
                    return a.getName().compareTo(b.getName());
                }
            });
            if (isTraining) {
                outFeatureVectors = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(wdResults, ConstantParameters.FILENAMEOFFeatureVectorData)), "UTF-8"));
            }
            File dirSeg = null;
            if (!isTraining) {
                dirSeg = new File(this.textFilesURL.getPath(), ConstantParameters.FILENAME_resultsDir);
                if (!dirSeg.exists()) dirSeg.mkdir();
            }
            int numDocs = 0;
            for (File f : xmlFiles) {
                if (!f.isDirectory()) {
                    ++numDocs;
                    Document doc = Factory.newDocument(f.toURI().toURL(), this.textCode);
                    doc.setName(f.getName());
                    System.out.println(numDocs + ", docName=" + doc.getName());
                    String text = doc.getContent().toString();
                    char[] chs = new char[text.length()];
                    int num;
                    StringBuffer letterNum = new StringBuffer();
                    num = convert2Chs(text, chs, letterNum);
                    String[] labels = new String[chs.length];
                    if (isTraining) {
                        num = obtainLabels(num, chs, labels);
                        labelsAndId.updateMultiLabelFromDoc(labels);
                    }
                    String[] termC1 = new String[num + 2];
                    String[] termC12 = new String[num + 1];
                    String[] termC13 = new String[num];
                    obtainTerms(num, chs, termC1, termC12, termC13);
                    if (isUpdateFeatList) {
                        updateFeatList(featuresList, termC1);
                        updateFeatList(featuresList, termC12);
                        updateFeatList(featuresList, termC13);
                    }
                    DocFeatureVectors docFV = new DocFeatureVectors();
                    docFV.docId = new String(doc.getName());
                    putFeatsIntoDocFV(featuresList, termC1, termC12, termC13, docFV);
                    LabelsOfFV[] multiLabels = new LabelsOfFV[num];
                    for (int j = 0; j < num; ++j) {
                        int[] labelsId = new int[1];
                        if (isTraining) labelsId[0] = new Integer(labelsAndId.label2Id.get(labels[j]).toString()).intValue(); else labelsId[0] = -1;
                        float[] labelPr = new float[1];
                        labelPr[0] = 1;
                        multiLabels[j] = new LabelsOfFV(1, labelsId, labelPr);
                    }
                    System.out.println("numInstance=" + docFV.numInstances);
                    BufferedWriter outSegText = null;
                    if (!isTraining) {
                        outFeatureVectors = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(wdResults, ConstantParameters.FILENAMEOFFeatureVectorData)), "UTF-8"));
                        outSegText = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(dirSeg, f.getName() + ".seg.txt")), this.textCode));
                    }
                    docFV.addDocFVsMultiLabelToFile(numDocs, outFeatureVectors, multiLabels);
                    if (!isTraining) {
                        outFeatureVectors.flush();
                        outFeatureVectors.close();
                    }
                    if (!isTraining) {
                        int[] selectedLabels = null;
                        selectedLabels = segementText(wdResults, this.learningAlg);
                        String[] terms = letterNum.toString().split(ConstantParameters.SEPARATTORLN);
                        int kk = 0;
                        StringBuffer textSeg = new StringBuffer();
                        for (int j = 0; j < num; ++j) {
                            String labelC = null;
                            String iObj = new Integer(selectedLabels[j] + 1).toString();
                            if (labelsAndId.id2Label.containsKey(iObj)) labelC = labelsAndId.id2Label.get(iObj).toString();
                            if (chs[j] == ConstantParameters.REPLACEMENT_Digit || chs[j] == ConstantParameters.REPLACEMENT_Letter || chs[j] == ConstantParameters.NEWLINE_Char) {
                                textSeg.append(terms[kk]);
                                if (labelC.equals(ConstantParameters.LABEL_R) || labelC.equals(ConstantParameters.LABEL_S)) textSeg.append(ConstantParameters.SEPARATTOR_BLANK);
                                ++kk;
                            } else if (chs[j] == ConstantParameters.REPLACEMENT_BLANK) {
                                textSeg.append(ConstantParameters.SEPARATTOR_BLANK);
                            } else {
                                textSeg.append(chs[j]);
                                if (labelC.equals(ConstantParameters.LABEL_R) || labelC.equals(ConstantParameters.LABEL_S)) textSeg.append(ConstantParameters.SEPARATTOR_BLANK);
                            }
                        }
                        outSegText.append(textSeg);
                        outSegText.flush();
                        outSegText.close();
                    }
                    Factory.deleteResource(doc);
                }
            }
            if (isTraining) {
                outFeatureVectors.flush();
                outFeatureVectors.close();
            }
            if (isUpdateFeatList) {
                featuresList.writeListIntoFile(wdResults, ConstantParameters.FILENAME_TERMS, this.textCode);
            }
            if (isTraining) {
                labelsAndId.writeLabelAndIdToFile(wdResults, ConstantParameters.FILENAMEOFLabelList);
            }
            if (isTraining) learningNewModel(wdResults, numDocs, this.learningAlg);
            System.out.println("Number of documents used is " + numDocs);
            System.out.println("Finished!");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ResourceInstantiationException e) {
            e.printStackTrace();
        } catch (GateException e) {
            e.printStackTrace();
        }
    }
