    private void writeThreadInformations(ThreadInformations threadInformations) throws DocumentException, IOException {
        final PdfPCell defaultCell = getDefaultCell();
        defaultCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        addCell(threadInformations.getName());
        defaultCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        if (threadInformations.isDaemon()) {
            addCell(getI18nString("oui"));
        } else {
            addCell(getI18nString("non"));
        }
        defaultCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        addCell(integerFormat.format(threadInformations.getPriority()));
        defaultCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        final PdfPCell cell = new PdfPCell();
        final Paragraph paragraph = new Paragraph(getDefaultCell().getLeading() + cellFont.getSize());
        paragraph.add(new Chunk(getImage("bullets/" + HtmlThreadInformationsReport.getStateIcon(threadInformations)), 0, -1));
        paragraph.add(new Phrase(String.valueOf(threadInformations.getState()), cellFont));
        cell.addElement(paragraph);
        currentTable.addCell(cell);
        if (stackTraceEnabled) {
            addCell(threadInformations.getExecutedMethod());
        }
        if (cpuTimeEnabled) {
            defaultCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            addCell(integerFormat.format(threadInformations.getCpuTimeMillis()));
            addCell(integerFormat.format(threadInformations.getUserTimeMillis()));
        }
    }
