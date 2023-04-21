    public static void main(String[] args) {
        System.out.println("Chapter 11: example Transparency2");
        System.out.println("-> Creates a PDF file demonstrating transparency.");
        System.out.println("-> jars needed: iText.jar");
        System.out.println("-> file generated: transparency2.pdf");
        Document document = new Document();
        try {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("results/in_action/chapter11/transparency2.pdf"));
            document.open();
            PdfContentByte cb = writer.getDirectContent();
            float gap = (document.getPageSize().getWidth() - 400) / 3;
            pictureBackdrop(gap, 500, cb, writer);
            pictureBackdrop(200 + 2 * gap, 500, cb, writer);
            pictureBackdrop(gap, 500 - 200 - gap, cb, writer);
            pictureBackdrop(200 + 2 * gap, 500 - 200 - gap, cb, writer);
            PdfTemplate tp;
            PdfTransparencyGroup group;
            tp = cb.createTemplate(200, 200);
            pictureCircles(0, 0, tp);
            group = new PdfTransparencyGroup();
            group.setIsolated(true);
            group.setKnockout(true);
            tp.setGroup(group);
            cb.addTemplate(tp, gap, 500);
            tp = cb.createTemplate(200, 200);
            pictureCircles(0, 0, tp);
            group = new PdfTransparencyGroup();
            group.setIsolated(true);
            group.setKnockout(false);
            tp.setGroup(group);
            cb.addTemplate(tp, 200 + 2 * gap, 500);
            tp = cb.createTemplate(200, 200);
            pictureCircles(0, 0, tp);
            group = new PdfTransparencyGroup();
            group.setIsolated(false);
            group.setKnockout(true);
            tp.setGroup(group);
            cb.addTemplate(tp, gap, 500 - 200 - gap);
            tp = cb.createTemplate(200, 200);
            pictureCircles(0, 0, tp);
            group = new PdfTransparencyGroup();
            group.setIsolated(false);
            group.setKnockout(false);
            tp.setGroup(group);
            cb.addTemplate(tp, 200 + 2 * gap, 500 - 200 - gap);
        } catch (DocumentException de) {
            System.err.println(de.getMessage());
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }
        document.close();
    }
