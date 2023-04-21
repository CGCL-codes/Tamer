    void toPdf() throws DocumentException, IOException {
        writeHeader();
        final PdfPCell defaultCell = getDefaultCell();
        boolean odd = false;
        for (final ThreadInformations threadInformations : threadInformationsList) {
            if (odd) {
                defaultCell.setGrayFill(0.97f);
            } else {
                defaultCell.setGrayFill(1);
            }
            odd = !odd;
            writeThreadInformations(threadInformations);
        }
        document.add(currentTable);
        final Paragraph tempsThreads = new Paragraph(I18N.getString("Temps_threads") + '\n', cellFont);
        tempsThreads.setAlignment(Element.ALIGN_RIGHT);
        document.add(tempsThreads);
    }
