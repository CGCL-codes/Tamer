    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == nextPage) {
            pagina += 1;
            if (pagina > paginas || pagina < 1) {
                pagina = 1;
            }
            try {
                File file = new File(ruta);
                RandomAccessFile raf = new RandomAccessFile(file, "r");
                FileChannel channel = raf.getChannel();
                ByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
                PDFFile pdffile = new PDFFile(buf);
                PDFPage page = pdffile.getPage(pagina);
                paginas = pdffile.getNumPages();
                panel.useZoomTool(false);
                panel.showPage(page);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            repaint();
            panel.repaint();
        }
        if (e.getSource() == backPage) {
            pagina -= 1;
            if (pagina > paginas || pagina < 1) {
                pagina = 1;
            }
            try {
                File file = new File(ruta);
                RandomAccessFile raf = new RandomAccessFile(file, "r");
                FileChannel channel = raf.getChannel();
                ByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
                PDFFile pdffile = new PDFFile(buf);
                PDFPage page = pdffile.getPage(pagina);
                paginas = pdffile.getNumPages();
                panel.useZoomTool(false);
                panel.showPage(page);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            repaint();
            panel.repaint();
        }
        if (e.getSource() == search) {
            number = Integer.parseInt(area1.getText());
            pagina = number;
            if (pagina > paginas || pagina < 0) {
                pagina = 1;
            }
            try {
                File file = new File(ruta);
                RandomAccessFile raf = new RandomAccessFile(file, "r");
                FileChannel channel = raf.getChannel();
                ByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
                PDFFile pdffile = new PDFFile(buf);
                PDFPage page = pdffile.getPage(pagina);
                paginas = pdffile.getNumPages();
                panel.useZoomTool(false);
                panel.showPage(page);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            repaint();
            panel.repaint();
        }
    }
