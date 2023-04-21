    public void delete(int pageCount, PdfPageData currentPageData, DeletePDFPages deletedPages) {
        File tempFile = null;
        try {
            tempFile = File.createTempFile("temp", null);
            ObjectStore.copy(selectedFile, tempFile.getAbsolutePath());
        } catch (Exception e) {
            return;
        }
        try {
            int[] pgsToDelete = deletedPages.getDeletedPages();
            if (pgsToDelete == null) return;
            int check = -1;
            if (pgsToDelete.length == 1) {
                check = currentGUI.showConfirmDialog(Messages.getMessage("PdfViewerMessage.ConfirmDeletePage"), Messages.getMessage("PdfViewerMessage.Confirm"), JOptionPane.YES_NO_OPTION);
            } else {
                check = currentGUI.showConfirmDialog(Messages.getMessage("PdfViewerMessage.ConfirmDeletePage"), Messages.getMessage("PdfViewerMessage.Confirm"), JOptionPane.YES_NO_OPTION);
            }
            if (check != 0) return;
            if (pgsToDelete == null) return;
            List pagesToDelete = new ArrayList();
            for (int i = 0; i < pgsToDelete.length; i++) pagesToDelete.add(new Integer(pgsToDelete[i]));
            PdfReader reader = new PdfReader(tempFile.getAbsolutePath());
            List bookmarks = SimpleBookmark.getBookmark(reader);
            SimpleBookmark.shiftPageNumbers(bookmarks, -1, new int[] { 5, 5 });
            boolean pageAdded = false;
            for (int page = 1; page <= pageCount; page++) {
                if (!pagesToDelete.contains(new Integer(page))) {
                    pageAdded = true;
                    page = pageCount;
                }
            }
            if (!pageAdded) {
                currentGUI.showMessageDialog(Messages.getMessage("PdfViewerError.PageWillNotDelete"));
                return;
            }
            Document document = new Document();
            PdfCopy writer = new PdfCopy(document, new FileOutputStream(selectedFile));
            document.open();
            for (int page = 1; page <= pageCount; page++) {
                if (!pagesToDelete.contains(new Integer(page))) {
                    PdfImportedPage pip = writer.getImportedPage(reader, page);
                    writer.addPage(pip);
                    pageAdded = true;
                }
            }
            writer.setOutlines(bookmarks);
            document.close();
        } catch (Exception e) {
            ObjectStore.copy(tempFile.getAbsolutePath(), selectedFile);
            e.printStackTrace();
        } finally {
            tempFile.delete();
        }
    }
