    void doPrint() throws Exception {
        JFileChooser fcOpen = new JFileChooser();
        fcOpen.setCurrentDirectory(new File(System.getProperty("user.dir")));
        fcOpen.setAcceptAllFileFilterUsed(false);
        fcOpen.setFileFilter(new javax.swing.filechooser.FileFilter() {

            public boolean accept(File f) {
                if (f.isDirectory()) return true;
                String s = f.getName();
                int i = s.lastIndexOf('.');
                if (i > 0 && i < s.length() - 1) {
                    String ext;
                    ext = s.substring(i + 1).toLowerCase();
                    if (ext.equals("pdf")) return true;
                }
                return false;
            }

            public String getDescription() {
                return "Accepts .pdf files";
            }
        });
        if (fcOpen.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) return;
        RandomAccessFile raf = new RandomAccessFile(fcOpen.getSelectedFile(), "r");
        FileChannel fc = raf.getChannel();
        ByteBuffer buf = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
        pdfFile = new PDFFile(buf);
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPrintable(this);
        try {
            HashPrintRequestAttributeSet attset;
            attset = new HashPrintRequestAttributeSet();
            attset.add(new PageRanges(1, pdfFile.getNumPages()));
            if (job.printDialog(attset)) job.print(attset);
        } catch (PrinterException pe) {
            JOptionPane.showMessageDialog(this, pe.getMessage());
        }
    }
