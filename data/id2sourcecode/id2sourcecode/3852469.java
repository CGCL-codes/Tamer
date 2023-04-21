    public void process(Analyzer analyzer, Filter filter) throws Exception {
        Statistics.dataSourceCount++;
        String filename = file.getName();
        if (!filter.filter(filename)) {
            if (file.isDirectory()) {
                new DirectorySource(file).process(analyzer, filter);
            } else {
                InputStream is = new FileInputStream(file);
                if (ZipFileSource.isZipFile(filename)) {
                    new ZipFileSource(is).process(analyzer, filter);
                } else {
                    analyzer.analyze(new DataArtifact(IOUtils.toByteArray(new FileInputStream(file)), filename));
                }
                is.close();
            }
        }
    }
