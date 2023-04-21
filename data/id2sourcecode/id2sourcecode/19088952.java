    public void nup(int pageCount, PdfPageData currentPageData, ExtractPDFPagesNup extractPage) {
        try {
            int[] pgsToEdit = extractPage.getPages();
            if (pgsToEdit == null) return;
            final String output_dir = extractPage.getRootDir() + separator + fileName + separator + "PDFs" + separator;
            File testDirExists = new File(output_dir);
            if (!testDirExists.exists()) testDirExists.mkdirs();
            List pagesToEdit = new ArrayList();
            for (int i = 0; i < pgsToEdit.length; i++) pagesToEdit.add(new Integer(pgsToEdit[i]));
            PdfReader reader = new PdfReader(selectedFile);
            File fileToSave = new File(output_dir + "export_" + fileName + ".pdf");
            if (fileToSave.exists()) {
                int n = currentGUI.showOverwriteDialog(fileToSave.getAbsolutePath(), false);
                if (n == 0) {
                } else {
                    return;
                }
            }
            int rows = extractPage.getLayoutRows();
            int coloumns = extractPage.getLayoutColumns();
            int paperWidth = extractPage.getPaperWidth();
            int paperHeight = extractPage.getPaperHeight();
            Rectangle pageSize = new Rectangle(paperWidth, paperHeight);
            String orientation = extractPage.getPaperOrientation();
            Rectangle newSize = null;
            if (orientation.equals(Messages.getMessage("PdfViewerNUPOption.Auto"))) {
                if (coloumns > rows) newSize = new Rectangle(pageSize.height(), pageSize.width()); else newSize = new Rectangle(pageSize.width(), pageSize.height());
            } else if (orientation.equals("Portrait")) {
                newSize = new Rectangle(pageSize.width(), pageSize.height());
            } else if (orientation.equals("Landscape")) {
                newSize = new Rectangle(pageSize.height(), pageSize.width());
            }
            String scale = extractPage.getScale();
            float leftRightMargin = extractPage.getLeftRightMargin();
            float topBottomMargin = extractPage.getTopBottomMargin();
            float horizontalSpacing = extractPage.getHorizontalSpacing();
            float verticalSpacing = extractPage.getVerticalSpacing();
            Rectangle unitSize = null;
            if (scale.equals("Auto")) {
                float totalHorizontalSpacing = (coloumns - 1) * horizontalSpacing;
                int totalWidth = (int) (newSize.width() - leftRightMargin * 2 - totalHorizontalSpacing);
                int unitWidth = totalWidth / coloumns;
                float totalVerticalSpacing = (rows - 1) * verticalSpacing;
                int totalHeight = (int) (newSize.height() - topBottomMargin * 2 - totalVerticalSpacing);
                int unitHeight = totalHeight / rows;
                unitSize = new Rectangle(unitWidth, unitHeight);
            } else if (scale.equals("Use Original Size")) {
                unitSize = null;
            } else if (scale.equals("Specified")) {
                unitSize = new Rectangle(extractPage.getScaleWidth(), extractPage.getScaleHeight());
            }
            int order = extractPage.getPageOrdering();
            int pagesPerPage = rows * coloumns;
            int repeats = 1;
            if (extractPage.getRepeat() == REPEAT_AUTO) repeats = coloumns * rows; else if (extractPage.getRepeat() == REPEAT_SPECIFIED) repeats = extractPage.getCopies();
            Document document = new Document(newSize, 0, 0, 0, 0);
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(fileToSave));
            document.open();
            PdfContentByte cb = writer.getDirectContent();
            PdfImportedPage importedPage;
            float offsetX = 0, offsetY = 0, factor;
            int actualPage = 0, page = 0;
            Rectangle currentSize;
            boolean isProportional = extractPage.isScaleProportional();
            for (int i = 1; i <= pageCount; i++) {
                if (pagesToEdit.contains(new Integer(i))) {
                    for (int j = 0; j < repeats; j++) {
                        int currentUnit = page % pagesPerPage;
                        if (currentUnit == 0) {
                            document.newPage();
                            actualPage++;
                        }
                        currentSize = reader.getPageSizeWithRotation(i);
                        if (unitSize == null) unitSize = currentSize;
                        int currentColoumn = 0, currentRow = 0;
                        if (order == ORDER_DOWN) {
                            currentColoumn = currentUnit / rows;
                            currentRow = currentUnit % rows;
                            offsetX = unitSize.width() * currentColoumn;
                            offsetY = newSize.height() - (unitSize.height() * (currentRow + 1));
                        } else if (order == ORDER_ACCROS) {
                            currentColoumn = currentUnit % coloumns;
                            currentRow = currentUnit / coloumns;
                            offsetX = unitSize.width() * currentColoumn;
                            offsetY = newSize.height() - (unitSize.height() * ((currentUnit / coloumns) + 1));
                        }
                        factor = Math.min(unitSize.width() / currentSize.width(), unitSize.height() / currentSize.height());
                        float widthFactor = factor, heightFactor = factor;
                        if (!isProportional) {
                            widthFactor = unitSize.width() / currentSize.width();
                            heightFactor = unitSize.height() / currentSize.height();
                        } else {
                            offsetX += ((unitSize.width() - (currentSize.width() * factor)) / 2f);
                            offsetY += ((unitSize.height() - (currentSize.height() * factor)) / 2f);
                        }
                        offsetX += (horizontalSpacing * currentColoumn) + leftRightMargin;
                        offsetY -= ((verticalSpacing * currentRow) + topBottomMargin);
                        importedPage = writer.getImportedPage(reader, i);
                        double rotation = currentSize.getRotation() * Math.PI / 180;
                        int mediaBoxX = -currentPageData.getMediaBoxX(i);
                        int mediaBoxY = -currentPageData.getMediaBoxY(i);
                        float a, b, c, d, e, f;
                        switch(currentSize.getRotation()) {
                            case 0:
                                a = widthFactor;
                                b = 0;
                                c = 0;
                                d = heightFactor;
                                e = offsetX + (mediaBoxX * widthFactor);
                                f = offsetY + (mediaBoxY * heightFactor);
                                cb.addTemplate(importedPage, a, b, c, d, e, f);
                                break;
                            case 90:
                                a = 0;
                                b = (float) (Math.sin(rotation) * -heightFactor);
                                c = (float) (Math.sin(rotation) * widthFactor);
                                d = 0;
                                e = offsetX + (mediaBoxY * widthFactor);
                                f = ((currentSize.height() * heightFactor) + offsetY) - (mediaBoxX * heightFactor);
                                cb.addTemplate(importedPage, a, b, c, d, e, f);
                                break;
                            case 180:
                                a = (float) (Math.cos(rotation) * widthFactor);
                                b = 0;
                                c = 0;
                                d = (float) (Math.cos(rotation) * heightFactor);
                                e = (offsetX + (currentSize.width() * widthFactor)) - (mediaBoxX * widthFactor);
                                f = ((currentSize.height() * heightFactor) + offsetY) - (mediaBoxY * heightFactor);
                                cb.addTemplate(importedPage, a, b, c, d, e, f);
                                break;
                            case 270:
                                a = 0;
                                b = (float) (Math.sin(rotation) * -heightFactor);
                                c = (float) (Math.sin(rotation) * widthFactor);
                                d = 0;
                                e = (offsetX + (currentSize.width() * widthFactor)) - (mediaBoxY * widthFactor);
                                f = offsetY + (mediaBoxX * heightFactor);
                                cb.addTemplate(importedPage, a, b, c, d, e, f);
                                break;
                        }
                        page++;
                    }
                }
            }
            document.close();
            currentGUI.showMessageDialog(Messages.getMessage("PdfViewerMessage.PagesSavedAsPdfTo") + " " + output_dir);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
