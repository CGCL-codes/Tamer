    private void copy(String reportFilename, String newReportFilename) {
        File src = new File(reportFilename);
        File outputDir = new File(FileUtils.getParent(this.outputFilename));
        File target = new File(outputDir, newReportFilename);
        FileUtils.copyFile(src, target);
    }
