    public TestReport rumImpl() throws Exception {
        initURLs();
        InputStream streamA = null;
        try {
            streamA = new BufferedInputStream(urlA.openStream());
        } catch (IOException e) {
            return reportException(ERROR_COULD_NOT_OPEN_IMAGE, e);
        }
        InputStream streamB = null;
        try {
            streamB = new BufferedInputStream(urlB.openStream());
        } catch (IOException e) {
            return reportException(ERROR_COULD_NOT_OPEN_IMAGE, e);
        }
        boolean accurate = false;
        try {
            accurate = compare(streamA, streamB);
        } catch (IOException e) {
            TestReport report = reportException(ERROR_WHILE_COMPARING_FILES, e);
            report.addDescriptionEntry(ENTRY_KEY_FIRST_IMAGE, urlA.toString());
            report.addDescriptionEntry(ENTRY_KEY_SECOND_IMAGE, urlB.toString());
            return report;
        }
        if (accurate) {
            return reportSuccess();
        }
        BufferedImage imageA = getImage(urlA);
        if (imageA == null) {
            TestReport report = reportError(ERROR_COULD_NOT_LOAD_IMAGE);
            report.addDescriptionEntry(ENTRY_KEY_IMAGE_URL, urlA.toString());
            return report;
        }
        BufferedImage imageB = getImage(urlB);
        if (imageB == null) {
            TestReport report = reportError(ERROR_COULD_NOT_LOAD_IMAGE);
            report.addDescriptionEntry(ENTRY_KEY_IMAGE_URL, urlB.toString());
            return report;
        }
        BufferedImage diff = buildDiffImage(imageA, imageB);
        BufferedImage cmp = buildCompareImage(imageA, imageB);
        File tmpDiff = imageToFile(diff, IMAGE_TYPE_DIFFERENCE);
        File tmpCmp = imageToFile(cmp, IMAGE_TYPE_COMPARISON);
        TestReport report = reportError(ERROR_DIFFERENCES);
        report.addDescriptionEntry(ENTRY_KEY_COMPARISON, tmpCmp);
        report.addDescriptionEntry(ENTRY_KEY_DIFFERENCE, tmpDiff);
        return report;
    }
