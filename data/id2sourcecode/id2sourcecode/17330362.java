    public PdfTemplate getAppearance() throws DocumentException, IOException {
        if (app[0] == null) {
            PdfTemplate t = app[0] = new PdfTemplate(writer);
            t.setBoundingBox(new Rectangle(100, 100));
            writer.addDirectTemplateSimple(t, new PdfName("n0"));
            t.setLiteral("% DSBlank\n");
        }
        if (app[1] == null && !acro6Layers) {
            PdfTemplate t = app[1] = new PdfTemplate(writer);
            t.setBoundingBox(new Rectangle(100, 100));
            writer.addDirectTemplateSimple(t, new PdfName("n1"));
            t.setLiteral(questionMark);
        }
        if (app[2] == null) {
            String text;
            if (layer2Text == null) {
                StringBuffer buf = new StringBuffer();
                buf.append("Digitally signed by ").append(PdfPKCS7.getSubjectFields((X509Certificate) certChain[0]).getField("CN")).append("\n");
                SimpleDateFormat sd = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss z");
                buf.append("Date: ").append(sd.format(signDate.getTime()));
                if (reason != null) buf.append("\n").append("Reason: ").append(reason);
                if (location != null) buf.append("\n").append("Location: ").append(location);
                text = buf.toString();
            } else text = layer2Text;
            PdfTemplate t = app[2] = new PdfTemplate(writer);
            t.setBoundingBox(rect);
            writer.addDirectTemplateSimple(t, new PdfName("n2"));
            Font font;
            if (layer2Font == null) font = new Font(); else font = new Font(layer2Font);
            float size = font.size();
            if (size <= 0) {
                Rectangle sr = new Rectangle(rect.width() - 2 * margin, rect.height() * (1 - topSection) - 2 * margin);
                size = fitText(font, text, sr, 12, runDirection);
            }
            ColumnText ct = new ColumnText(t);
            ct.setRunDirection(runDirection);
            ct.setSimpleColumn(new Phrase(text, font), margin, 0, rect.width() - margin, rect.height() * (1 - topSection) - margin, size, Element.ALIGN_LEFT);
            ct.go();
        }
        if (app[3] == null && !acro6Layers) {
            PdfTemplate t = app[3] = new PdfTemplate(writer);
            t.setBoundingBox(new Rectangle(100, 100));
            writer.addDirectTemplateSimple(t, new PdfName("n3"));
            t.setLiteral("% DSBlank\n");
        }
        if (app[4] == null && !acro6Layers) {
            PdfTemplate t = app[4] = new PdfTemplate(writer);
            t.setBoundingBox(new Rectangle(0, rect.height() * (1 - topSection), rect.right(), rect.top()));
            writer.addDirectTemplateSimple(t, new PdfName("n4"));
            Font font;
            if (layer2Font == null) font = new Font(); else font = new Font(layer2Font);
            float size = font.size();
            String text = "Signature Not Verified";
            if (layer4Text != null) text = layer4Text;
            Rectangle sr = new Rectangle(rect.width() - 2 * margin, rect.height() * topSection - 2 * margin);
            size = fitText(font, text, sr, 15, runDirection);
            ColumnText ct = new ColumnText(t);
            ct.setRunDirection(runDirection);
            ct.setSimpleColumn(new Phrase(text, font), margin, 0, rect.width() - margin, rect.height() - margin, size, Element.ALIGN_LEFT);
            ct.go();
        }
        int rotation = writer.reader.getPageRotation(page);
        Rectangle rotated = new Rectangle(rect);
        int n = rotation;
        while (n > 0) {
            rotated = rotated.rotate();
            n -= 90;
        }
        if (frm == null) {
            frm = new PdfTemplate(writer);
            frm.setBoundingBox(rotated);
            writer.addDirectTemplateSimple(frm, new PdfName("FRM"));
            float scale = Math.min(rect.width(), rect.height()) * 0.9f;
            float x = (rect.width() - scale) / 2;
            float y = (rect.height() - scale) / 2;
            scale /= 100;
            if (rotation == 90) frm.concatCTM(0, 1, -1, 0, rect.height(), 0); else if (rotation == 180) frm.concatCTM(-1, 0, 0, -1, rect.width(), rect.height()); else if (rotation == 270) frm.concatCTM(0, -1, 1, 0, 0, rect.width());
            frm.addTemplate(app[0], 0, 0);
            if (!acro6Layers) frm.addTemplate(app[1], scale, 0, 0, scale, x, y);
            frm.addTemplate(app[2], 0, 0);
            if (!acro6Layers) {
                frm.addTemplate(app[3], scale, 0, 0, scale, x, y);
                frm.addTemplate(app[4], 0, 0);
            }
        }
        PdfTemplate napp = new PdfTemplate(writer);
        napp.setBoundingBox(rotated);
        writer.addDirectTemplateSimple(napp, null);
        napp.addTemplate(frm, 0, 0);
        return napp;
    }
