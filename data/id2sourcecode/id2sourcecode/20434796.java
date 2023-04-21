    private static void concatenaPDFs(Document doc, PdfWriter writer, Entrada ent) throws Exception {
        ArrayList<Entrada> al = ent.getHijos();
        PdfContentByte cb = writer.getDirectContent();
        for (int i = 0; i < al.size(); i++) {
            Entrada hij = (Entrada) al.get(i);
            if (hij.isSelectedResource() == -1) continue;
            InputStream gen = null;
            if (hij.getNombre().toLowerCase().endsWith(".lrf")) {
                Book book = new Book(hij.getFile());
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                book.getPDF(bos, false, new Hashtable<String, String>());
                gen = new ByteArrayInputStream(bos.toByteArray());
            } else {
                gen = new FileInputStream(hij.getFile());
            }
            PdfReader reader = new PdfReader(gen);
            int numPagesToImport = reader.getNumberOfPages();
            for (int j = 1; j <= numPagesToImport; j++) {
                doc.newPage();
                PdfImportedPage page = writer.getImportedPage(reader, j);
                if (j == 1) {
                    cb.localDestination(hij.getCanonicalName(), new PdfDestination(PdfDestination.FIT));
                    System.out.println("\tA�adiendo " + hij.getCanonicalName());
                }
                cb.addTemplate(page, 0, 0);
            }
            reader.close();
        }
        for (int i = 0; i < al.size(); i++) {
            Entrada hij = (Entrada) al.get(i);
            if (hij.isDir()) {
                concatenaPDFs(doc, writer, hij);
                writer.flush();
            }
        }
    }
