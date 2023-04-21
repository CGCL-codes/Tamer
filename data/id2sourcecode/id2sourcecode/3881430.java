    private void writeKanji() {
        document.open();
        try {
            ReadingFile readMachine = new ReadingFile();
            readMachine.readKanji();
            LinkedList<String> words = readMachine.getFinalList();
            PdfPTable table = new PdfPTable(3);
            table.setWidthPercentage(100);
            boolean nowKanji = false;
            short count = 0;
            for (String str : words) {
                if (count % 9 == 0) {
                    count = 0;
                    nowKanji = !nowKanji;
                }
                Paragraph para = null;
                if (nowKanji) {
                    para = new Paragraph(str, kanjiFont);
                } else {
                    str = str.replaceAll("\\|", "\n\n");
                    para = new Paragraph(str, furiganaFont);
                }
                PdfPCell cell = new PdfPCell(para);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setFixedHeight((float) (842.0 / 3));
                table.addCell(cell);
                count++;
            }
            document.add(table);
            document.close();
            System.out.println("Finished !");
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }
