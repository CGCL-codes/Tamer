    public static void main(String[] args) {
        String filename = "Resume";
        ArrayList<String> ids = null;
        String[] fieldStr = new String[] { "Id", "Title", "Status", "Description", "Analysis" };
        boolean generatePdf = true;
        boolean generateXls = true;
        Options opts = new Options(args, Options.Multiplicity.ZERO_OR_ONE, 1, 100);
        opts.getSet().addOption("pdf").addOption("xls").addOption("file", Options.Separator.EQUALS).addOption("fields", Options.Separator.EQUALS);
        if (!opts.check()) {
            System.out.println("Usage:\n\t recoverSpr [-pdf] [-xls] [-file=<file>]  [-fields=<field1>:<..>:<fieldn>]<id> ... <id>");
            System.exit(1);
        }
        generatePdf = opts.getSet().isSet("pdf");
        generateXls = opts.getSet().isSet("xls");
        if (opts.getSet().isSet("file")) {
            filename = opts.getSet().getOption("file").getResultValue(0);
        }
        if (opts.getSet().isSet("fields")) {
            String fieldsOption = opts.getSet().getOption("fields").getResultValue(0);
            fieldStr = fieldsOption.split(":");
        }
        ids = opts.getSet().getData();
        if (ids != null && ids.size() > 0) {
            com.lowagie.text.Document pdfDoc = null;
            TocGenerator tocG = null;
            int chapIndx = 1;
            WritableWorkbook w = null;
            WritableSheet s = null;
            try {
                if (generatePdf) {
                    tocG = new TocGenerator();
                    pdfDoc = RecoverSpr.openPdfDocument(filename + ".pdf", tocG);
                }
                if (generateXls) {
                    w = Workbook.createWorkbook(new File(filename + ".xls"));
                    s = w.createSheet("PHS", 0);
                }
                Image headerImage = Image.getInstance(RecoverSpr.class.getResource("/image/header_50.jpg"));
                headerImage.scalePercent(60);
                DOMParser parser = new DOMParser();
                Vector fields = new Vector(Arrays.asList(fieldStr));
                Collections.sort(ids);
                for (int i = 0; i < ids.size(); i++) {
                    RecoverSpr rs = new RecoverSpr(parser, ids.get(i));
                    if (generatePdf) {
                        Paragraph chapTitle = new Paragraph(rs.getData("Id"), FontFactory.getFont(FontFactory.HELVETICA, 16));
                        chapTitle.setSpacingAfter(12);
                        pdfDoc.add(new Chapter(chapTitle, chapIndx));
                        chapIndx++;
                        rs.addPdfTable(pdfDoc, headerImage, fields);
                    }
                    if (generateXls) {
                        rs.generateXlsRow(s, i, fields);
                    }
                    System.out.println(rs.toString(fields));
                }
                if (generatePdf) {
                    pdfDoc.add(new Chapter("Index", chapIndx));
                    pdfDoc.add(tocG.getToc());
                    pdfDoc.close();
                }
                if (generateXls) {
                    w.write();
                    w.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
