    public static Image getSchedule(final PdfWriter writer, final Set<ScheduleItem> items, final String type, final String color) throws IOException, DocumentException {
        final PdfReader reader = new PdfReader(getSchedule(items, type, color));
        final PdfImportedPage page = writer.getImportedPage(reader, 1);
        return Image.getInstance(page);
    }
