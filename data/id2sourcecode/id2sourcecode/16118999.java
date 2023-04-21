    public void createPdf(final String eventUri) throws IOException, DocumentException {
        final Rectangle rect = Dimensions.getDimension(true, Dimension.BODY);
        final Document document = new Document(rect, 0, 0, 0, 0);
        final PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(INFO.getOutput()));
        writer.setInitialLeading(18);
        document.open();
        Image image;
        Event event = EventDAO.getEvent(eventUri);
        Date today = event.getFrom();
        Paragraph p;
        ConferenceDay[] days = MyProperties.getDays();
        for (int i = 0; i < days.length; i++) {
            image = getSchedule(writer, ScheduleDAO.getScheduleForDay(eventUri, i + 1), days[i].getType(), days[i].getTitle());
            if (writer.getVerticalPosition(false) - image.getScaledHeight() < rect.getBottom()) {
                document.newPage();
            }
            p = new Paragraph(MyObjectFactory.dateFormat(today), MyFonts.DATE);
            p.setAlignment(Paragraph.ALIGN_RIGHT);
            document.add(p);
            today = MyObjectFactory.incrementDate(today, 1);
            document.add(image);
        }
        final PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(90);
        table.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        PdfReader reader = new PdfReader(MyProperties.getPlan1());
        table.addCell(Image.getInstance(writer.getImportedPage(reader, 1)));
        reader = new PdfReader(MyProperties.getPlan2());
        table.addCell(Image.getInstance(writer.getImportedPage(reader, 1)));
        document.add(table);
        document.close();
    }
