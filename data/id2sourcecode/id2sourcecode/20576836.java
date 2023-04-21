    protected void createJobScriptAndConfigurationFiles(FileWriter writer) throws Exception {
        AnalysisPipeline16sTask ap16sTask = (AnalysisPipeline16sTask) task;
        File configFile = new File(getSGEConfigurationDirectory() + File.separator + CONFIG_PREFIX + "1");
        AnalysisPipeline16sTask tmpTask = (AnalysisPipeline16sTask) task;
        FileWriter fos = null;
        try {
            fos = new FileWriter(new File(resultFileNode.getDirectoryPath() + File.separator + AnalysisPipeline16SResultNode.TAG_PRIMER_FASTA));
            fos.write(">" + tmpTask.getParameter(AnalysisPipeline16sTask.PARAM_primer1Defline) + "\n");
            fos.write(tmpTask.getParameter(AnalysisPipeline16sTask.PARAM_primer1Sequence) + "\n");
            fos.write(">" + tmpTask.getParameter(AnalysisPipeline16sTask.PARAM_primer2Defline) + "\n");
            fos.write(tmpTask.getParameter(AnalysisPipeline16sTask.PARAM_primer2Sequence) + "\n");
        } finally {
            if (null != fos) {
                fos.close();
            }
        }
        String finalQualityConfigPath = resultFileNode.getDirectoryPath() + File.separator + AnalysisPipeline16SResultNode.TAG_QUALITY_CONFIG;
        FileWriter configWriter = new FileWriter(finalQualityConfigPath);
        try {
            configWriter.write("### final cutoffs for layout clustering\n");
            configWriter.write("cutoff=.1,.05,.03,.02,.01,.005\n");
            configWriter.write("minovllen=200\n");
            configWriter.write("maxerr=.15\n\n");
            configWriter.write("# Primer percent identity threshold for trimming\n");
            configWriter.write("primerIdent=75\n");
            configWriter.write("# initial length & QV filtering of frg files\n");
            configWriter.write("read_len_min=" + Integer.valueOf(task.getParameter(AnalysisPipeline16sTask.PARAM_readLengthMinimum)) + "\n");
            configWriter.write("avg_qv_min=" + Integer.valueOf(task.getParameter(AnalysisPipeline16sTask.PARAM_minAvgQV)) + "\n");
            configWriter.write("# Maximum number of N's in a good read\n");
            configWriter.write("maxNCnt=" + Integer.valueOf(task.getParameter(AnalysisPipeline16sTask.PARAM_maxNCount)) + "\n\n");
            configWriter.write("### SSU ref db match thresholds\n");
            configWriter.write("# minium number of identies allowed in alignment\n");
            configWriter.write("minIdent=" + Integer.valueOf(task.getParameter(AnalysisPipeline16sTask.PARAM_minIdentCount)) + "\n\n");
            configWriter.write("# minimum percent of query covered by alignment\n");
            configWriter.write("minQueryCovg=.3\n\n");
            configWriter.write("# filter if percent identical is less then pIdent &&\n");
            configWriter.write("# less then pLen of query is aligned\n");
            configWriter.write("pIdent=.7\n");
            configWriter.write("pLen=.5\n");
        } finally {
            configWriter.close();
        }
        boolean fileSuccess = configFile.createNewFile();
        if (!fileSuccess) {
            throw new ServiceException("Unable to create a config file for the 16S pipeline.");
        }
        String outputFilenamePrefix = ap16sTask.getParameter(AnalysisPipeline16sTask.PARAM_filenamePrefix);
        createShellScript(outputFilenamePrefix, writer, finalQualityConfigPath);
        setJobIncrementStop(1);
    }
