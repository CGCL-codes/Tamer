    private void previsualizarPdf(int index) {
        Pdf p = listado.getPdf(index);
        File f = new File(clienteActual.getIdCliente() + clienteActual.getNomSinEspacios() + clienteActual.getApelSinEspacios() + "/" + p.getName());
        try {
            if (raf != null) {
                raf.close();
            }
            raf = new RandomAccessFile(f, "r");
            FileChannel channel = raf.getChannel();
            ByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
            PDFFile pdffile = new PDFFile(buf);
            PDFPage page = pdffile.getPage(0);
            ((PagePanel) panelPrev).showPage(page);
        } catch (FileNotFoundException e1) {
            JOptionPane.showMessageDialog(null, e1.getMessage());
        } catch (IOException e2) {
            JOptionPane.showMessageDialog(null, e2.getMessage());
        }
    }
