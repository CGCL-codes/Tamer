    public void render(Result result) throws IOException {
        CsvWriter csvWriter = new CsvWriter(this.outputStream, this.delimiter, this.charset);
        csvWriter.write("Algorithm");
        csvWriter.write("Problem");
        csvWriter.write("CPU Time [ms]");
        csvWriter.write("System Time [ms]");
        csvWriter.write("User Time [ms]");
        csvWriter.write("Clock Time [ms]");
        csvWriter.write("Optimize counter");
        csvWriter.write("Optimize/sec (CPU) [1/s]");
        csvWriter.write("Optimize/sec (Clock) [1/s]");
        csvWriter.write("Exception");
        csvWriter.write("Best solution");
        csvWriter.write("Best solution (human readable)");
        csvWriter.write("Depth");
        csvWriter.write("Fitness");
        csvWriter.write("Operations");
        csvWriter.endRecord();
        for (ResultEntry resultEntry : result.getResultEntries()) {
            PreciseTimestamp start = resultEntry.getStartTimestamp();
            PreciseTimestamp stop = resultEntry.getStopTimestamp();
            csvWriter.write(resultEntry.getAlgorithm().toString());
            csvWriter.write(resultEntry.getProblem().toString());
            csvWriter.write(Long.toString(start.getCpuTimeSpent(stop)));
            csvWriter.write(Long.toString(start.getSystemTimeSpent(stop)));
            csvWriter.write(Long.toString(start.getUserTimeSpent(stop)));
            csvWriter.write(Long.toString(start.getClockTimeSpent(stop)));
            csvWriter.write(Long.toString(resultEntry.getOptimizeCounter()));
            csvWriter.write(Long.toString(resultEntry.getOptimizeCounter() * 1000L / start.getCpuTimeSpent(stop)));
            csvWriter.write(Long.toString(resultEntry.getOptimizeCounter() * 1000L / start.getClockTimeSpent(stop)));
            csvWriter.write(resultEntry.getException() == null ? "none" : resultEntry.getException().getClass().toString());
            if (resultEntry.getBestConfiguration() == null) {
                csvWriter.write("none");
                csvWriter.write("-");
                csvWriter.write("-");
                csvWriter.write("-");
            } else {
                StringBuffer stringBufferHumanReadable = new StringBuffer("[");
                StringBuffer stringBuffer = new StringBuffer("[");
                ConfigurationMap configurationMap = resultEntry.getProblem().getConfigurationMap();
                for (int i = 0; i < resultEntry.getBestConfiguration().getDimension(); ++i) {
                    stringBuffer.append(i == 0 ? "" : ", ").append(configurationMap.map(resultEntry.getBestConfiguration().valueAt(i), i));
                    stringBufferHumanReadable.append(i == 0 ? "" : ", ").append(resultEntry.getBestConfiguration().valueAt(i));
                }
                stringBuffer.append("]");
                stringBufferHumanReadable.append("]");
                csvWriter.write(stringBuffer.toString());
                csvWriter.write(stringBufferHumanReadable.toString());
                csvWriter.write(Long.toString(resultEntry.getBestConfiguration().getOperationHistory().getCounter()));
                csvWriter.write(Double.toString(resultEntry.getBestFitness()));
                for (OperationHistory operationHistory : resultEntry.getBestConfiguration().getOperationHistory().getChronologicalList()) {
                    csvWriter.write(operationHistory.getOperation().toString());
                }
            }
            csvWriter.endRecord();
        }
        csvWriter.flush();
    }
