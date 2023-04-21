    protected void iteratePages(PdfDictionary page, PdfReader pdfreader, ArrayList<PdfDictionary> pageInh, int count_in_leaf, PdfWriter writer) throws IOException {
        float curwidth;
        float curheight;
        PdfArray kidsPR = page.getAsArray(PdfName.KIDS);
        if (kidsPR == null) {
            PdfArray arr = page.getAsArray(PdfName.MEDIABOX);
            curwidth = Float.parseFloat(arr.getPdfObject(2).toString());
            curheight = Float.parseFloat(arr.getPdfObject(3).toString());
            PdfNumber rotation = page.getAsNumber(PdfName.ROTATE);
            if (rotation == null) {
                System.out.println("optional rotation missing");
                rotation = new PdfNumber(0);
            }
            Ausrichtung ausr = new Ausrichtung(rotation.floatValue(), new Rectangle(curwidth, curheight));
            switch(ausr.type) {
                case Ausrichtung.A4Landscape:
                case Ausrichtung.A3Portrait:
                    ausr.rotate();
                    page.put(PdfName.ROTATE, new PdfNumber(ausr.getRotation()));
                    System.out.println("rotate page:" + (pagecount + 1) + " targetformat: " + ausr);
                    this.pagecountrotatedpages++;
                    break;
            }
            curwidth = ausr.getM5();
            curheight = ausr.getM6();
            if (((pagecount + 1) % 2) == 0) {
                if ((Math.abs(curwidth - width) > tolerancex) || (Math.abs(curheight - height) > tolerancey)) {
                    Seitehinzufuegen(page, count_in_leaf, writer, arr);
                    this.pagecountinsertedpages++;
                }
            }
            if (((pagecount + 1) % 2) == 1) {
                width = curwidth;
                height = curheight;
                lastpage = page;
            }
            pageInh.add(pagecount, page);
            pagecount++;
        } else {
            page.put(PdfName.TYPE, PdfName.PAGES);
            for (int k = 0; k < kidsPR.size(); ++k) {
                PdfDictionary kid = kidsPR.getAsDict(k);
                iteratePages(kid, pdfreader, pageInh, k, writer);
            }
        }
    }
