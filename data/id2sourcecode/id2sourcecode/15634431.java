    public static void main(String[] args) throws IOException, DocumentException {
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(new File("./target/test-classes/examples/tiger.pdf")));
        document.open();
        Reader reader = new InputStreamReader(SvgTest.class.getResourceAsStream("tiger.svg"));
        PdfTemplate template = XMLHelperForSVG.getInstance().parseToTemplate(writer.getDirectContent(), reader);
        Image img = Image.getInstance(template);
        img.setBorder(Image.BOX);
        img.setBorderWidth(1);
        document.add(img);
        document.close();
    }
