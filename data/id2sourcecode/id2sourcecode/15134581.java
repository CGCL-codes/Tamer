    private void initiateVCFInDifferentOutmode(SAMSequenceDictionary refDict) {
        File outputVcfFile = new File(BAC.vfn1);
        writer = new TcgaVCFWriter(outputVcfFile, refDict, true);
        writer.setRefSource(getToolkit().getArguments().referenceFile.toString());
        writer.writeHeader(new VCFHeader(getHeaderInfo(), samples));
        if (getToolkit().getArguments().numberOfThreads > 1) {
            multiThreadWriter = new SortingTcgaVCFWriter(writer, MAXIMUM_CACHE_FOR_OUTPUT_VCF);
            if (BAC.ovd) {
                File outputVerboseFile = new File(BAC.fnovd);
                verboseWriter = new TcgaVCFWriter(outputVerboseFile, refDict, false);
                verboseWriter.writeHeader(new VCFHeader(getHeaderInfo(), samples));
            }
        }
        if (BAC.OutputMode == OUTPUT_MODE.DEFAULT_FOR_TCGA) {
            File outputAdditionalVcfFile = new File(BAC.vfn2);
            additionalWriterForDefaultTcgaMode = new TcgaVCFWriter(outputAdditionalVcfFile, refDict, true);
            additionalWriterForDefaultTcgaMode.writeHeader(new VCFHeader(getHeaderInfo(), samples));
            if (getToolkit().getArguments().numberOfThreads > 1) {
                multiAdditionalWriterForDefaultTcgaMode = new SortingTcgaVCFWriter(additionalWriterForDefaultTcgaMode, MAXIMUM_CACHE_FOR_OUTPUT_VCF);
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
