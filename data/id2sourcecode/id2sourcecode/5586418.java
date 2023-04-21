    public byte[] getImageReportForSettings(final ReportModel report, final FormModel settingsForm, final ServiceCall call) {
        try {
            System.out.println("Report for " + report.getReportRef().getDisplay() + " patient " + settingsForm.getPatientId() + " " + settingsForm.getVisitId());
            ByteWrapper byteWrapper = ReportServer.getPdfReportForSettings(report.getReportRef().getId(), settingsForm, call);
            if (byteWrapper != null) {
                File pdfFile = SystemUtil.getTemporaryFile("pdf");
                FileSystemUtil.createBinaryFile(pdfFile, byteWrapper.getBytes());
                FileUtils.copyFile(pdfFile, new File("C:\\temp\\test.pdf"));
                BufferedImage image = PDFUtility.createImageFromPDF(pdfFile, 1);
                if (image != null) {
                    File tempFile = SystemUtil.getTemporaryFile("png");
                    ImageUtil.savePNG(image, tempFile);
                    byte[] bytes = FileSystemUtil.getBinaryContents(tempFile);
                    FileSystemUtil.createBinaryFile(new File("C:\\temp\\test.png"), bytes);
                    return bytes;
                } else {
                    System.err.println("null image");
                    return null;
                }
            } else {
                System.err.println("null byte wrapper");
                return null;
            }
        } catch (Exception ex) {
            Log.exception(ex);
            return null;
        }
    }
