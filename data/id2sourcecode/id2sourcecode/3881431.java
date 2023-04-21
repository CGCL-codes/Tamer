    private void writeKanjiCards() {
        ReaderSQL rsql = new ReaderSQL();
        rsql.readKanjiFromDB();
        LinkedList<KanjiCard> kanjiCardList = rsql.getKanjiCardsList();
        document.open();
        createCells(kanjiCardList);
        invertCells();
        PdfPTable table = addCells();
        try {
            document.add(table);
            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        System.out.println("Finished !");
    }
