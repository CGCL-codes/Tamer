    public PdfTemplate getAppearance() throws DocumentException {
        if (isInvisible()) {
            PdfTemplate t = new PdfTemplate(writer);
            t.setBoundingBox(new Rectangle(0, 0));
            writer.addDirectTemplateSimple(t, null);
            return t;
        }
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
                buf.append("Digitally signed by ").append(PdfPKCS7.getSubjectFields((X509Certificate) certChain[0]).getField("CN")).append('\n');
                SimpleDateFormat sd = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss z");
                buf.append("Date: ").append(sd.format(signDate.getTime()));
                if (reason != null) buf.append('\n').append("Reason: ").append(reason);
                if (location != null) buf.append('\n').append("Location: ").append(location);
                text = buf.toString();
            } else text = layer2Text;
            PdfTemplate t = app[2] = new PdfTemplate(writer);
            t.setBoundingBox(rect);
            writer.addDirectTemplateSimple(t, new PdfName("n2"));
            if (image != null) {
                if (imageScale == 0) {
                    t.addImage(image, rect.getWidth(), 0, 0, rect.getHeight(), 0, 0);
                } else {
                    float usableScale = imageScale;
                    if (imageScale < 0) usableScale = Math.min(rect.getWidth() / image.getWidth(), rect.getHeight() / image.getHeight());
                    float w = image.getWidth() * usableScale;
                    float h = image.getHeight() * usableScale;
                    float x = (rect.getWidth() - w) / 2;
                    float y = (rect.getHeight() - h) / 2;
                    t.addImage(image, w, 0, 0, h, x, y);
                }
            }
            Font font;
            if (layer2Font == null) font = new Font(); else font = new Font(layer2Font);
            float size = font.getSize();
            Rectangle dataRect = null;
            Rectangle signatureRect = null;
            if (render == SignatureRenderNameAndDescription || (render == SignatureRenderGraphicAndDescription && this.signatureGraphic != null)) {
                signatureRect = new Rectangle(MARGIN, MARGIN, rect.getWidth() / 2 - MARGIN, rect.getHeight() - MARGIN);
                dataRect = new Rectangle(rect.getWidth() / 2 + MARGIN / 2, MARGIN, rect.getWidth() - MARGIN / 2, rect.getHeight() - MARGIN);
                if (rect.getHeight() > rect.getWidth()) {
                    signatureRect = new Rectangle(MARGIN, rect.getHeight() / 2, rect.getWidth() - MARGIN, rect.getHeight());
                    dataRect = new Rectangle(MARGIN, MARGIN, rect.getWidth() - MARGIN, rect.getHeight() / 2 - MARGIN);
                }
            } else {
                dataRect = new Rectangle(MARGIN, MARGIN, rect.getWidth() - MARGIN, rect.getHeight() * (1 - TOP_SECTION) - MARGIN);
            }
            if (render == SignatureRenderNameAndDescription) {
                String signedBy = PdfPKCS7.getSubjectFields((X509Certificate) certChain[0]).getField("CN");
                Rectangle sr2 = new Rectangle(signatureRect.getWidth() - MARGIN, signatureRect.getHeight() - MARGIN);
                float signedSize = fitText(font, signedBy, sr2, -1, runDirection);
                ColumnText ct2 = new ColumnText(t);
                ct2.setRunDirection(runDirection);
                ct2.setSimpleColumn(new Phrase(signedBy, font), signatureRect.getLeft(), signatureRect.getBottom(), signatureRect.getRight(), signatureRect.getTop(), signedSize, Element.ALIGN_LEFT);
                ct2.go();
            } else if (render == SignatureRenderGraphicAndDescription) {
                ColumnText ct2 = new ColumnText(t);
                ct2.setRunDirection(runDirection);
                ct2.setSimpleColumn(signatureRect.getLeft(), signatureRect.getBottom(), signatureRect.getRight(), signatureRect.getTop(), 0, Element.ALIGN_RIGHT);
                Image im = Image.getInstance(signatureGraphic);
                im.scaleToFit(signatureRect.getWidth(), signatureRect.getHeight());
                Paragraph p = new Paragraph();
                float x = 0;
                float y = -im.getScaledHeight() + 15;
                x = x + (signatureRect.getWidth() - im.getScaledWidth()) / 2;
                y = y - (signatureRect.getHeight() - im.getScaledHeight()) / 2;
                p.add(new Chunk(im, x + (signatureRect.getWidth() - im.getScaledWidth()) / 2, y, false));
                ct2.addElement(p);
                ct2.go();
            }
            if (size <= 0) {
                Rectangle sr = new Rectangle(dataRect.getWidth(), dataRect.getHeight());
                size = fitText(font, text, sr, 12, runDirection);
            }
            ColumnText ct = new ColumnText(t);
            ct.setRunDirection(runDirection);
            ct.setSimpleColumn(new Phrase(text, font), dataRect.getLeft(), dataRect.getBottom(), dataRect.getRight(), dataRect.getTop(), size, Element.ALIGN_LEFT);
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
            t.setBoundingBox(new Rectangle(0, rect.getHeight() * (1 - TOP_SECTION), rect.getRight(), rect.getTop()));
            writer.addDirectTemplateSimple(t, new PdfName("n4"));
            Font font;
            if (layer2Font == null) font = new Font(); else font = new Font(layer2Font);
            float size = font.getSize();
            String text = "Signature Not Verified";
            if (layer4Text != null) text = layer4Text;
            Rectangle sr = new Rectangle(rect.getWidth() - 2 * MARGIN, rect.getHeight() * TOP_SECTION - 2 * MARGIN);
            size = fitText(font, text, sr, 15, runDirection);
            ColumnText ct = new ColumnText(t);
            ct.setRunDirection(runDirection);
            ct.setSimpleColumn(new Phrase(text, font), MARGIN, 0, rect.getWidth() - MARGIN, rect.getHeight() - MARGIN, size, Element.ALIGN_LEFT);
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
            float scale = Math.min(rect.getWidth(), rect.getHeight()) * 0.9f;
            float x = (rect.getWidth() - scale) / 2;
            float y = (rect.getHeight() - scale) / 2;
            scale /= 100;
            if (rotation == 90) frm.concatCTM(0, 1, -1, 0, rect.getHeight(), 0); else if (rotation == 180) frm.concatCTM(-1, 0, 0, -1, rect.getWidth(), rect.getHeight()); else if (rotation == 270) frm.concatCTM(0, -1, 1, 0, 0, rect.getWidth());
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
