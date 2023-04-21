    private void init() throws FileNotFoundException {
        PdfReader reader = null;
        PdfWriter writer = null;
        try {
            reader = new PdfReader(getStreamStatic(this.fileNameIn));
            int n = reader.getNumberOfPages();
            Rectangle psize = reader.getPageSize(1);
            psize.setRight(psize.getRight() - this.templateOffsetX);
            psize.setTop(psize.getTop() - this.templateOffsetY);
            this.width = (int) psize.getWidth();
            float height = psize.getHeight();
            int MARGIN = 32;
            this.document = new Document(psize, MARGIN, MARGIN, MARGIN, MARGIN);
            if (!this.directoryOut.exists()) {
                this.directoryOut.mkdirs();
            }
            System.err.println("Directory out " + this.directoryOut.getAbsolutePath());
            File f = new File(this.directoryOut, this.fileNameOut);
            if (f.exists()) {
                f.renameTo(new File(this.directoryOut, "Old" + this.fileNameOut));
                f = new File(this.directoryOut, this.fileNameOut);
            }
            System.err.println("Creation du fichier " + f.getAbsolutePath());
            writer = PdfWriter.getInstance(this.document, new FileOutputStream(f));
            this.document.open();
            this.cb = writer.getDirectContent();
            System.out.println("There are " + n + " pages in the document.");
            this.document.newPage();
            PdfImportedPage page1 = writer.getImportedPage(reader, 1);
            this.cb.addTemplate(page1, -this.templateOffsetX, -this.templateOffsetY);
            this.bf = BaseFont.createFont(BaseFont.TIMES_ROMAN, BaseFont.CP1252, BaseFont.EMBEDDED);
            this.bfb = BaseFont.createFont(BaseFont.TIMES_BOLD, BaseFont.CP1252, BaseFont.EMBEDDED);
        } catch (FileNotFoundException fE) {
            throw fE;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }
