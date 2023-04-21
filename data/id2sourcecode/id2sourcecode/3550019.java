    public void create(Screen... screens) {
        try {
            int width = 0, height = 2 * _margin;
            for (Screen screen : screens) {
                if (screen._img.getWidth() > width) {
                    width = screen._img.getWidth();
                }
                height += screen._img.getHeight() + 80;
            }
            width += 2 * _margin;
            Document pdf = new Document(new Rectangle(width, height), _margin, _margin, _margin, _margin);
            PdfWriter.getInstance(pdf, new FileOutputStream(_filename));
            pdf.open();
            for (int i = 0; i < screens.length; i++) {
                if (i > 0) {
                    pdf.add(new Paragraph("\n\n"));
                }
                _addScreen(pdf, screens[i]);
            }
            pdf.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
