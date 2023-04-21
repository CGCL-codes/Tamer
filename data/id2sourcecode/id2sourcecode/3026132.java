    private void cmd_archive() {
        boolean success = false;
        ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
        org.openXpertya.print.pdf.text.Document document = null;
        org.openXpertya.print.pdf.text.pdf.PdfWriter writer = null;
        try {
            if (m_reportEngine.getLayout().getPageable(false) instanceof LayoutEngine) {
                LayoutEngine layoutengine = (LayoutEngine) (m_reportEngine.getLayout().getPageable(false));
                CPaper cpaper = layoutengine.getPaper();
                int i = (int) cpaper.getWidth(true);
                int j = (int) cpaper.getHeight(true);
                int k = 0;
                do {
                    if (k >= layoutengine.getNumberOfPages()) {
                        break;
                    }
                    if (document == null) {
                        document = new org.openXpertya.print.pdf.text.Document(new org.openXpertya.print.pdf.text.Rectangle(i, j));
                        writer = org.openXpertya.print.pdf.text.pdf.PdfWriter.getInstance(document, bytearrayoutputstream);
                        document.open();
                    }
                    if (document != null) {
                        org.openXpertya.print.pdf.text.pdf.DefaultFontMapper mapper = new org.openXpertya.print.pdf.text.pdf.DefaultFontMapper();
                        org.openXpertya.print.pdf.text.FontFactory.registerDirectories();
                        mapper.insertDirectory("c:\\windows\\fonts");
                        org.openXpertya.print.pdf.text.pdf.PdfContentByte cb = writer.getDirectContent();
                        org.openXpertya.print.pdf.text.pdf.PdfTemplate tp = cb.createTemplate(i, j);
                        java.awt.Graphics2D g2 = tp.createGraphics(i, j, mapper);
                        layoutengine.print(g2, layoutengine.getPageFormat(), k);
                        g2.dispose();
                        cb.addTemplate(tp, 0, 0);
                        document.newPage();
                    }
                    k++;
                } while (true);
            }
            if (document != null) {
                document.close();
            }
            bytearrayoutputstream.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        byte[] data = bytearrayoutputstream.toByteArray();
        if (data != null) {
            MArchive archive = new MArchive(Env.getCtx(), m_reportEngine.getPrintInfo(), null);
            archive.setBinaryData(data);
            success = archive.save();
        }
        if (success) {
            ADialog.info(m_WindowNo, this, "Archived");
        } else {
            ADialog.error(m_WindowNo, this, "ArchiveError");
        }
    }
