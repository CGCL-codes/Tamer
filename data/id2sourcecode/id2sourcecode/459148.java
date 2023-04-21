    public void Handle(SourceReader sourceReader, Document pdfDoc, PdfWriter writer) throws Exception {
        Rectangle pagesize = new Rectangle(PageSize.A4);
        pagesize.setBackgroundColor(new java.awt.Color(0x64, 0x95, 0xed));
        Document document = pdfDoc;
        pagesize.setGrayFill(0.9f);
        pagesize.setBorder(Rectangle.BOX);
        pagesize.setBorderWidth(2);
        pagesize.setBorderColor(java.awt.Color.CYAN);
        document.setPageSize(pagesize);
        document.open();
        PdfPTable table = new PdfPTable(8);
        float[] rows = { 100f, 100f, 100f, 100f, 100f, 100f, 100f, 100f };
        table.setTotalWidth(rows);
        table.setSpacingBefore(10);
        for (int k = 1; k <= 100; ++k) {
            table.addCell("row " + k);
            table.addCell("row " + k);
            table.addCell("row " + k);
            table.addCell("row " + k);
            table.addCell("row " + k);
            table.addCell("row " + k);
            table.addCell("row " + k);
            table.addCell("row " + k);
        }
        document.add(new Paragraph("Hi welcome to doXen..."));
        document.add(new Paragraph("This is a sample pdf..."));
        document.add(new Paragraph(""));
        document.add(table);
        document.close();
        String filename = sourceReader.readLine();
        USERID = "12345";
        USERNAME = "USER";
        TOMAILID = "USER@YAHOO.COM";
        CCMAILID = "USER@GMAIL.COM";
        USERPASSWORD = "USER";
        OWNERPASSWORD = "12345";
        OUTPUTFILENAME = filename;
        FROMEMAILID = "ADMIN@YAHOO.COM";
        SUBJECT = "YOUR MONTHLY STATEMENT";
    }
