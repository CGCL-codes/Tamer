    public static boolean isValidReportFile(File repFile) {
        try {
            if (repFile != null && repFile.exists()) {
                ReportSchemaValidator validator = new ReportSchemaValidator();
                ReportValidationErrorHandler errors = validator.validate(new FileReader(repFile));
                if (errors.noErrors()) {
                    return true;
                } else {
                    logger.warn("Submitted report files has schema errors, will overwrite: " + errors.toString());
                    return false;
                }
            }
            return false;
        } catch (IOException e) {
            logger.warn("Error reading report file, will overwrite: " + e.getMessage(), e);
            return false;
        } catch (SAXException e) {
            logger.warn("Error reading report file, will overwrite: " + e.getMessage(), e);
            return false;
        }
    }
