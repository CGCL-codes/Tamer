    public void initialize() {
        Set<String> samples = new TreeSet<String>();
        if (BAC.ASSUME_SINGLE_SAMPLE != null) {
            samples = SampleUtils.getSAMFileSamples(getToolkit().getSAMFileHeader());
            if (!samples.isEmpty()) {
                System.out.println("sample name provided was masked by bam file header");
            } else {
                samples.add(BAC.ASSUME_SINGLE_SAMPLE);
            }
        } else {
            samples = SampleUtils.getSAMFileSamples(getToolkit().getSAMFileHeader());
            System.out.println("samples provided: " + samples.toString());
            if (samples.isEmpty()) {
                System.err.println("No sample name provided, program will automately provide the bam file header: " + samples.toString());
            }
        }
        BG_engine = new BisulfiteGenotyperEngine(getToolkit(), BAC, logger, samples);
        SAMSequenceDictionary refDict = getToolkit().getMasterSequenceDictionary();
        if (autoEstimateC) {
            if (secondIteration) {
                File outputVcfFile = new File(BAC.vfn1);
                writer = new TcgaVCFWriter(outputVcfFile, refDict, false);
                writer.setRefSource(getToolkit().getArguments().referenceFile.toString());
                writer.writeHeader(new VCFHeader(getHeaderInfo(), samples));
                if (getToolkit().getArguments().numberOfThreads > 1) {
                    multiThreadWriter = new SortingTcgaVCFWriter(writer, MAXIMUM_CACHE_FOR_OUTPUT_VCF);
                    multiThreadWriter.enableDiscreteLoci(BAC.lnc);
                    if (BAC.ovd) {
                        File outputVerboseFile = new File(BAC.fnovd);
                        verboseWriter = new TcgaVCFWriter(outputVerboseFile, refDict, false);
                        verboseWriter.writeHeader(new VCFHeader(getHeaderInfo(), samples));
                    }
                }
                if (BAC.OutputMode == BisulfiteGenotyperEngine.OUTPUT_MODE.DEFAULT_FOR_TCGA) {
                    File outputAdditionalVcfFile = new File(BAC.vfn2);
                    additionalWriterForDefaultTcgaMode = new TcgaVCFWriter(outputAdditionalVcfFile, refDict, false);
                    additionalWriterForDefaultTcgaMode.writeHeader(new VCFHeader(getHeaderInfo(), samples));
                    if (getToolkit().getArguments().numberOfThreads > 1) {
                        multiAdditionalWriterForDefaultTcgaMode = new SortingTcgaVCFWriter(additionalWriterForDefaultTcgaMode, MAXIMUM_CACHE_FOR_OUTPUT_VCF);
                        multiAdditionalWriterForDefaultTcgaMode.enableDiscreteLoci(BAC.lnc);
                    }
                }
                if (BAC.orad) {
                    File outputBamFile = new File(BAC.fnorad);
                    SAMFileWriterFactory samFileWriterFactory = new SAMFileWriterFactory();
                    samFileWriterFactory.setCreateIndex(true);
                    samWriter = samFileWriterFactory.makeBAMWriter(getToolkit().getSAMFileHeader(), false, outputBamFile);
                }
                if (BAC.fnocrd != null) {
                    File outputReadsDetailFile = new File(BAC.fnocrd);
                    if (BAC.sequencingMode == MethylSNPModel.GM) {
                        readsWriter = new NOMeSeqReadsWriterImp(outputReadsDetailFile);
                    } else {
                        readsWriter = new cpgReadsWriterImp(outputReadsDetailFile);
                    }
                    if (getToolkit().getArguments().numberOfThreads > 1) {
                        if (BAC.sequencingMode == MethylSNPModel.GM) {
                            multiThreadCpgReadsWriter = new SortingNOMeSeqReadsWriter(readsWriter, MAXIMUM_CACHE_FOR_OUTPUT_READS);
                        } else {
                            multiThreadCpgReadsWriter = new SortingCpgReadsWriter(readsWriter, MAXIMUM_CACHE_FOR_OUTPUT_READS);
                        }
                        multiThreadCpgReadsWriter.writeHeader(true);
                    } else {
                        readsWriter.addHeader(true);
                    }
                }
            } else {
            }
        } else {
            File outputVcfFile = new File(BAC.vfn1);
            writer = new TcgaVCFWriter(outputVcfFile, refDict, false);
            writer.setRefSource(getToolkit().getArguments().referenceFile.toString());
            writer.writeHeader(new VCFHeader(getHeaderInfo(), samples));
            if (getToolkit().getArguments().numberOfThreads > 1) {
                multiThreadWriter = new SortingTcgaVCFWriter(writer, MAXIMUM_CACHE_FOR_OUTPUT_VCF);
                multiThreadWriter.enableDiscreteLoci(BAC.lnc);
            }
            if (BAC.OutputMode == BisulfiteGenotyperEngine.OUTPUT_MODE.DEFAULT_FOR_TCGA) {
                File outputAdditionalVcfFile = new File(BAC.vfn2);
                additionalWriterForDefaultTcgaMode = new TcgaVCFWriter(outputAdditionalVcfFile, refDict, false);
                additionalWriterForDefaultTcgaMode.writeHeader(new VCFHeader(getHeaderInfo(), samples));
                if (getToolkit().getArguments().numberOfThreads > 1) {
                    multiAdditionalWriterForDefaultTcgaMode = new SortingTcgaVCFWriter(additionalWriterForDefaultTcgaMode, MAXIMUM_CACHE_FOR_OUTPUT_VCF);
                    multiAdditionalWriterForDefaultTcgaMode.enableDiscreteLoci(BAC.lnc);
                }
            }
            if (BAC.orad) {
                File outputBamFile = new File(BAC.fnorad);
                SAMFileWriterFactory samFileWriterFactory = new SAMFileWriterFactory();
                samFileWriterFactory.setCreateIndex(true);
                samWriter = samFileWriterFactory.makeBAMWriter(getToolkit().getSAMFileHeader(), false, outputBamFile);
            }
            if (BAC.fnocrd != null) {
                File outputReadsDetailFile = new File(BAC.fnocrd);
                if (BAC.sequencingMode == MethylSNPModel.GM) {
                    readsWriter = new NOMeSeqReadsWriterImp(outputReadsDetailFile);
                } else {
                    readsWriter = new cpgReadsWriterImp(outputReadsDetailFile);
                }
                if (getToolkit().getArguments().numberOfThreads > 1) {
                    if (BAC.sequencingMode == MethylSNPModel.GM) {
                        multiThreadCpgReadsWriter = new SortingNOMeSeqReadsWriter(readsWriter, MAXIMUM_CACHE_FOR_OUTPUT_READS);
                    } else {
                        multiThreadCpgReadsWriter = new SortingCpgReadsWriter(readsWriter, MAXIMUM_CACHE_FOR_OUTPUT_READS);
                    }
                    multiThreadCpgReadsWriter.writeHeader(true);
                } else {
                    readsWriter.addHeader(true);
                }
            }
        }
        if (!secondIteration) summary = new CytosineTypeStatus(BAC);
    }
