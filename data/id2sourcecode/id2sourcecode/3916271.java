    public TIFF_Document getTiff(String PXIId, int startPage, int endPage, String outLocation) throws ImageNotFoundException, ImageArchiveException {
        ImageDocument doc = (ImageDocument) images.get(PXIId);
        if (doc == null) throw new ImageNotFoundException("Images not in Dummy" + PXIId);
        int totalPages = doc.getTotalPages();
        if (endPage > totalPages) throw new ImageNotFoundException("ImagePage(s) not in Dummy" + PXIId + "-" + endPage);
        if (startPage < 1) throw new ImageNotFoundException("ImagePage(s) not in Dummy" + PXIId + "-" + startPage);
        TIFF_Document res = new TIFF_Document();
        try {
            for (int i = startPage; i <= endPage; i++) {
                String sourceFile = root.getAbsolutePath() + File.separator + PXIId.trim() + File.separator + Utils.formatString(i, "000000") + ".TIF";
                String targetFile = outLocation + File.separator + Utils.formatString(i, "000000") + ".TIF";
                Utils.writeBinaryFile(targetFile, Utils.readBinaryFile(sourceFile));
                res.addPage(new TIFF_Page(targetFile));
            }
        } catch (IOException e) {
            throw new ImageArchiveException(e.toString());
        } catch (ImageDataException e) {
            throw new ImageArchiveException(e.toString());
        }
        return res;
    }
