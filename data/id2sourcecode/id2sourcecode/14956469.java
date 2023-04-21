        public void run() {
            if (map == null || map.size() <= 0) {
                return;
            }
            bar.setValue(50);
            value1 = 0.0f;
            value2 = 0.0f;
            value3 = 0.0f;
            value4 = 0.0f;
            averageSim = 0.0f;
            similarityvaluevector = new ArrayList<Float>();
            similaritystrvaluevector = new ArrayList<Float>();
            model = new ArrayList<String>();
            f = new ArrayList<Float>();
            aB = new ArrayList<Float>();
            aS = new ArrayList<Float>();
            LogReader logReader = null;
            String miningAlgorithmName;
            Class miningAlgorithmClass;
            String similarityAlgorithmName;
            Class similarityAlgorithmClass;
            String similarityStrAlgorithmName;
            Class similarityStrAlgorithmClass;
            String miningmodelfolder = System.getProperty("user.dir", "") + "/miningmodel";
            File modelFolder = new File(miningmodelfolder);
            if (!modelFolder.exists()) modelFolder.mkdir();
            String resultFile = System.getProperty("user.dir", "") + "/result.xls";
            File resultfile = new File(resultFile);
            if (!resultfile.exists()) try {
                resultfile.createNewFile();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
            FileWriter fw = null;
            BufferedWriter bw = null;
            try {
                fw = new FileWriter(resultFile);
                bw = new BufferedWriter(fw);
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                bw.write("FileName", 0, 8);
                bw.write("\t");
                bw.write("BehSimilarity", 0, 13);
                bw.write("\t");
                bw.write("StrSimilarity", 0, 13);
                bw.write("\t");
                bw.write("Time", 0, 4);
                bw.write("\t");
                bw.write("aS", 0, 2);
                bw.write("\t");
                bw.write("f", 0, 1);
                bw.write("\t");
                bw.write("aB", 0, 2);
                bw.newLine();
                bw.flush();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
            try {
                Enumeration<String> en = map.keys();
                while (en.hasMoreElements()) {
                    String logpath = en.nextElement();
                    String modelpath = modellogmap.get(logpath);
                    String minepath = logminemap.get(logpath);
                    FileInputStream in1 = null;
                    PnmlImport input = null;
                    PetriNet pn = null;
                    LogFile logFile = LogFile.getInstance(logpath);
                    logReader = LogReaderFactory.createInstance(null, logFile);
                    miningAlgorithmName = miningAlgorithmList.get(selectedMiningAlgorithm);
                    miningAlgorithmClass = Class.forName(miningAlgorithmName);
                    Object miningAlgorithmObject = miningAlgorithmClass.newInstance();
                    Class ptype[] = new Class[1];
                    ptype[0] = Class.forName("org.processmining.framework.log.LogReader");
                    Method miningMethod = miningAlgorithmClass.getMethod("mine", ptype);
                    Object m_args[] = new Object[1];
                    m_args[0] = logReader;
                    Long startTime = System.nanoTime();
                    PetriNet miningModel = (PetriNet) miningMethod.invoke(miningAlgorithmObject, m_args);
                    Long estimatedTime = System.nanoTime() - startTime;
                    String outputFile = logpath;
                    outputFile = outputFile.replaceFirst("log", "miningmodel");
                    outputFile = outputFile.replaceFirst("mxml", "pnml");
                    if (miningModel != null) {
                        PnmlExport exportPlugin = new PnmlExport();
                        Object[] objects = new Object[] { miningModel };
                        ProvidedObject object = new ProvidedObject("temp", objects);
                        File file = new File(outputFile);
                        if (file.exists()) {
                            file.delete();
                        }
                        file.createNewFile();
                        FileOutputStream outputStream = new FileOutputStream(outputFile);
                        exportPlugin.export(object, outputStream);
                        outputStream.close();
                    } else {
                        System.err.println("No Petri net could be constructed.");
                    }
                    bar.setValue(75);
                    similarityAlgorithmName = similarityAlgorithmList.get(selectedSimilarityAlgorithm);
                    similarityAlgorithmClass = Class.forName(similarityAlgorithmName);
                    Object similarityAlgorithmObject = similarityAlgorithmClass.newInstance();
                    Class stype[] = new Class[2];
                    stype[0] = Class.forName("org.processmining.framework.models.petrinet.PetriNet");
                    stype[1] = Class.forName("org.processmining.framework.models.petrinet.PetriNet");
                    Method similarityMethod = similarityAlgorithmClass.getMethod("similarity", stype);
                    Object s_args[] = new Object[2];
                    s_args[0] = map.get(logpath);
                    s_args[1] = miningModel;
                    Object result = similarityMethod.invoke(similarityAlgorithmObject, s_args);
                    similarityStrAlgorithmName = similarityStrAlgorithmList.get(selectedStrSimilarityAlgorithm);
                    similarityStrAlgorithmClass = Class.forName(similarityStrAlgorithmName);
                    Object similarityStrAlgorithmObject = similarityStrAlgorithmClass.newInstance();
                    Class stype1[] = new Class[2];
                    stype1[0] = Class.forName("org.processmining.framework.models.petrinet.PetriNet");
                    stype1[1] = Class.forName("org.processmining.framework.models.petrinet.PetriNet");
                    Method similarityStrMethod = similarityStrAlgorithmClass.getMethod("similarity", stype1);
                    Object s_args1[] = new Object[2];
                    s_args1[0] = map.get(logpath);
                    s_args1[1] = miningModel;
                    Object result1 = similarityStrMethod.invoke(similarityStrAlgorithmObject, s_args1);
                    miningModel.clearGraph();
                    miningModel.delete();
                    (map.get(logpath)).clearGraph();
                    (map.get(logpath)).delete();
                    System.out.println(logpath + " the BehSimilarity is " + Float.valueOf(result.toString()));
                    System.out.println(logpath + " the StrSimilarity is " + Float.valueOf(result1.toString()));
                    String rs = result.toString();
                    String rs1 = result1.toString();
                    Float ress = 0f;
                    Float ress1 = 0f;
                    if (!rs.equals("NaN")) {
                        Float res = Float.parseFloat(rs);
                        ress = (float) (((int) (res * 100)) / 100.0);
                        similarityvaluevector.add(ress);
                        model.add("");
                    }
                    if (!rs1.equals("NaN")) {
                        Float res1 = Float.parseFloat(rs1);
                        ress1 = (float) (((int) (res1 * 100)) / 100.0);
                        similaritystrvaluevector.add(ress1);
                    }
                    int indexx = modelpath.lastIndexOf("\\");
                    String name = modelpath.substring(indexx + 1, modelpath.length());
                    bw.write(name, 0, name.length());
                    bw.write("\t");
                    bw.write(ress.toString(), 0, ress.toString().length());
                    bw.write("\t");
                    bw.write(ress1.toString(), 0, ress1.toString().length());
                    bw.write("\t");
                    bw.write(estimatedTime.toString(), 0, estimatedTime.toString().length());
                    bw.write("\t");
                    try {
                        in1 = new FileInputStream(minepath);
                        input = new PnmlImport();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    try {
                        pn = input.read(in1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    StructuralAnalysisResult sar = new StructuralAnalysisResult(new AnalysisConfiguration(), pn, new StructuralAnalysisMethod(pn));
                    float aSr = sar.getStructuralAppropriatenessMeasure();
                    Float aSre = (float) (((int) (aSr * 100)) / 100.0);
                    aS.add(aSre);
                    try {
                        bw.write(aSre.toString(), 0, aSre.toString().length());
                        bw.write("\t");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    AnalysisConfiguration myOptions = createAnalysisConfiguration();
                    LogReplayAnalysisMethod logReplayAnalysis = new LogReplayAnalysisMethod(pn, logReader, new ConformanceMeasurer(), new Progress(0, 100));
                    int maxSearchDepth = MaximumSearchDepthDiagnosis.determineMaximumSearchDepth(pn);
                    logReplayAnalysis.setMaxDepth(maxSearchDepth);
                    ConformanceLogReplayResult clrr = (ConformanceLogReplayResult) logReplayAnalysis.analyse(myOptions);
                    float fr = clrr.getFitnessMeasure();
                    Float fre = (float) (((int) (fr * 100)) / 100.0);
                    f.add(fre);
                    try {
                        bw.write(fre.toString(), 0, fre.toString().length());
                        bw.write("\t");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    float ab = 0;
                    try {
                        ab = clrr.getBehavioralAppropriatenessMeasure();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    Float abb = (float) (((int) (ab * 100)) / 100.0);
                    aB.add(abb);
                    try {
                        bw.write(abb.toString(), 0, abb.toString().length());
                        bw.newLine();
                        bw.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                bw.close();
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("similaritystrvaluevector :" + similaritystrvaluevector);
            System.out.println("similarityvaluevector :" + similarityvaluevector);
            System.out.println("f :" + f);
            System.out.println("aB :" + aB);
            System.out.println("aS :" + aS);
            float s1 = 0;
            float s2 = 0;
            for (int i = 0; i < similarityvaluevector.size(); i++) {
                s1 += similarityvaluevector.get(i);
                s2 += similarityvaluevector.get(i) * similarityvaluevector.get(i);
            }
            float value11 = s1 / similarityvaluevector.size();
            float ex2 = s2 / similarityvaluevector.size();
            float dx = ex2 - value11 * value11;
            float value22 = (float) Math.sqrt(dx);
            value1 = (float) (((int) (value11 * 100)) / 100.0);
            value2 = (float) (((int) (value22 * 100)) / 100.0);
            averageSimilarityField.setText(value1.toString());
            meanDeviationField.setText(value2.toString());
            float s3 = 0;
            float s4 = 0;
            for (int i = 0; i < similaritystrvaluevector.size(); i++) {
                s3 += similaritystrvaluevector.get(i);
                s4 += similaritystrvaluevector.get(i) * similaritystrvaluevector.get(i);
            }
            float value33 = s3 / similaritystrvaluevector.size();
            float ex3 = s2 / similaritystrvaluevector.size();
            float dx1 = ex3 - value33 * value33;
            float value44 = (float) Math.sqrt(dx);
            value3 = (float) (((int) (value33 * 100)) / 100.0);
            value4 = (float) (((int) (value44 * 100)) / 100.0);
            averageStrSimilarityField.setText(value3.toString());
            meanStrDeviationField.setText(value4.toString());
            averageSim = (value1 + value3) / 2;
            averageSim = (float) (((int) (averageSim * 1000)) / 1000.0);
            averageBehStrSimField.setText(averageSim.toString());
            tpane.setHistogramTitle("", "Model");
            tpane.setSim(similarityvaluevector);
            tpane.setStrsim(similaritystrvaluevector);
            tpane.setaB(aB);
            tpane.setaS(aS);
            tpane.setF(f);
            tpane.repaint();
            simcurve.setHistogramTitle("Similarity", "Model");
            simcurve.setSim(similarityvaluevector);
            simcurve.setStrsim(similaritystrvaluevector);
            simcurve.repaint();
            fabscurve.setHistogramTitle("", "Model");
            fabscurve.setaB(aB);
            fabscurve.setaS(aS);
            fabscurve.setF(f);
            fabscurve.repaint();
            map.clear();
            bar.setValue(100);
        }
