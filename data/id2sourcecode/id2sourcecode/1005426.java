    private void selectFile() {
        JFileChooser open = new JFileChooser(".");
        open.setFileSelectionMode(JFileChooser.FILES_ONLY);
        String[] pdf = new String[] { "pdf" };
        open.addChoosableFileFilter(new FileFilterer(pdf, "Pdf (*.pdf)"));
        int resultOfFileSelect = JFileChooser.ERROR_OPTION;
        while (resultOfFileSelect == JFileChooser.ERROR_OPTION) {
            resultOfFileSelect = open.showOpenDialog(this);
            if (resultOfFileSelect == JFileChooser.ERROR_OPTION) System.err.println("JFileChooser error");
            if (resultOfFileSelect == JFileChooser.APPROVE_OPTION) {
                currentFile = open.getSelectedFile().getAbsolutePath();
                currentPage = 1;
                try {
                    pdfDecoder.closePdfFile();
                    pdfDecoder.openPdfFile(currentFile);
                    if (!checkEncryption()) {
                        resultOfFileSelect = JFileChooser.CANCEL_OPTION;
                    }
                    pdfDecoder.decodePage(currentPage);
                    pdfDecoder.setPageParameters(1, 1);
                    pdfDecoder.invalidate();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                pageCounter2.setText(String.valueOf(currentPage));
                pageCounter3.setText("of " + pdfDecoder.getPageCount());
                setTitle(viewerTitle + " - " + currentFile);
                repaint();
            }
        }
    }
