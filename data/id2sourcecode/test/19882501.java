    @PrePersist
    public void zipReportContent(Report report) {
        try {
            if (report.getReportContent() != null) {
                sLog.info("Zipping report content");
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream);
                zipOutputStream.putNextEntry(new ZipEntry("0"));
                zipOutputStream.write(report.getReportContent());
                zipOutputStream.closeEntry();
                report.setZippedReportContent(byteArrayOutputStream.toByteArray());
                zipOutputStream.close();
                sLog.info("Zipped size : " + report.getZippedReportContent().length);
            }
        } catch (Exception ex) {
            sLog.error("Error zipping report content", ex);
        }
    }
