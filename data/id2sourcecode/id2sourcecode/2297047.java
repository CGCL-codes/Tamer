    private static void createPdf(String filename) {
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(filename));
            document.open();
            Paragraph hello = new Paragraph("(English:) hello, " + "(Esperanto:) he, alo, saluton, (Latin:) heu, ave, " + "(French:) allô, (Italian:) ciao, (German:) hallo, he, heda, holla, " + "(Portuguese:) alô, olá, hei, psiu, bom día, (Dutch:) hallo, dag, " + "(Spanish:) ola, eh, (Catalan:) au, bah, eh, ep, " + "(Swedish:) hej, hejsan(Danish:) hallo, dav, davs, goddag, hej, " + "(Norwegian:) hei; morn, (Papiamento:) halo; hallo; kí tal, " + "(Faeroese:) halló, hoyr, (Turkish:) alo, merhaba, (Albanian:) tungjatjeta");
            document.add(new Paragraph("1. To the Universe:"));
            document.add(hello);
            document.newPage();
            document.add(new Paragraph("2. to the World:"));
            document.add(hello);
            document.newPage();
            document.add(new Paragraph("3. to the Sun:"));
            document.add(hello);
            document.newPage();
            document.add(new Paragraph("4. to the Moon:"));
            document.add(hello);
            document.newPage();
            document.add(new Paragraph("5. to the Stars:"));
            document.add(hello);
            document.newPage();
            document.add(new Paragraph("6. To the People:"));
            document.add(hello);
            document.newPage();
            document.add(new Paragraph("7. to mothers and fathers:"));
            document.add(hello);
            document.newPage();
            document.add(new Paragraph("8. to brothers and sisters:"));
            document.add(hello);
            document.newPage();
            document.add(new Paragraph("9. to wives and husbands:"));
            document.add(hello);
            document.newPage();
            document.add(new Paragraph("10. to sons and daughters:"));
            document.add(hello);
            document.newPage();
            document.add(new Paragraph("11. to complete strangers:"));
            document.add(hello);
            document.newPage();
            document.add(new Paragraph("12. To the Animals:"));
            document.add(hello);
            document.newPage();
            document.add(new Paragraph("13. o cats and dogs:"));
            document.add(hello);
            document.newPage();
            document.add(new Paragraph("14. to birds and bees:"));
            document.add(hello);
            document.newPage();
            document.add(new Paragraph("15. to farm animals and wild animals:"));
            document.add(hello);
            document.newPage();
            document.add(new Paragraph("16. to bugs and beatles:"));
            document.add(hello);
            document.newPage();
            document.add(new Paragraph("17. to fish and shellfish:"));
            document.add(hello);
        } catch (DocumentException de) {
            System.err.println(de.getMessage());
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }
        document.close();
    }
