    public static boolean isUnmodifiedSourceForReportFile(File repFile, String reportSource) {
        try {
            if (repFile != null && repFile.exists()) {
                DAO dao = DAOFactory.getInstance().getDAO(reportSource, ConverterData.getInstance().getDaoFormat());
                SearchResultIdentifier originalSRI = dao.getSearchResultIdentifier();
                ReportReader reader = new ReportReader(repFile);
                SearchResultIdentifier reportSRI = reader.getSearchResultIdentifier();
                return reportSRI.getHash().equals(originalSRI.getHash());
            }
            return false;
        } catch (InvalidFormatException e) {
            logger.warn("Error reading report file, will overwrite: " + e.getMessage(), e);
            return false;
        }
    }
