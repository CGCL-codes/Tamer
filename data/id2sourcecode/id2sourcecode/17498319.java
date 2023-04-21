    @Test
    public void readCatalog() throws Exception {
        CatalogWriter writer = new CatalogWriter();
        CatalogReader reader = new CatalogReader();
        QuestionCatalog catalog = new QuestionCatalog();
        catalog.setSubjectName("Testfach");
        Question q = new Question();
        q.setUpperText("upperText");
        q.setLowerText("lowerText");
        BufferedImage im1 = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        q.setImage(im1);
        Answer answerA = new Answer("Antwort A", true, "A");
        Answer answerB = new Answer("Antwort B", false, "B");
        BufferedImage im2 = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        answerB.setImage(im2);
        q.addAnswer(answerA);
        q.addAnswer(answerB);
        im1.createGraphics().drawString("Bild", 10, 10);
        im2.createGraphics().drawString("Antowrt-Bild", 10, 10);
        File f = File.createTempFile("read-write-test", ".opplc");
        writer.wirteCatalog(catalog, new FileOutputStream(f));
        QuestionCatalog catalog2 = reader.readCatalog(f);
        assertEquals(catalog, catalog2);
        catalog2.setSubjectName("Testfach ....");
        assertFalse(catalog.equals(catalog2));
    }
