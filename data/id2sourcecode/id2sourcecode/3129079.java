    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        writer = (HtmlWriter) in.readObject();
        writer.resetCurrentContainer();
        logDirectory = (File) in.readObject();
        logCurrent = (File) in.readObject();
        logOld = (File) in.readObject();
        logIndexFile = (File) in.readObject();
        summary = (HtmlSummaryReporter) in.readObject();
        lastTestFileName = (String) in.readObject();
        lastTestStatus = (Integer) in.readObject();
        lastTestClassName = (String) in.readObject();
        startTestTime = (Long) in.readObject();
        reportDir = (String) in.readObject();
        isTemp = (Boolean) in.readObject();
        isZipLogDisable = (Boolean) in.readObject();
    }
