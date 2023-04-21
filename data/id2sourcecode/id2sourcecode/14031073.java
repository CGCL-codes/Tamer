    public void exportStudy(boolean includeInterview) throws CorruptedInterviewException {
        JFileChooser exportFileChooser = new JFileChooser("Export Study...");
        File exportFile;
        boolean complete = false;
        if (currentStudy.first() != null) exportFileChooser.setCurrentDirectory(currentStudy.first().getParentFile());
        exportFileChooser.addChoosableFileFilter(new ExtensionFileFilter("Rich Text Format (RTF)", "rtf"));
        exportFileChooser.addChoosableFileFilter(new ExtensionFileFilter("Portable Document Format (PDF)", "pdf"));
        while (!complete) {
            if (JFileChooser.APPROVE_OPTION == exportFileChooser.showSaveDialog(parent)) {
                try {
                    int confirm = JOptionPane.OK_OPTION;
                    FileFilter filter = exportFileChooser.getFileFilter();
                    if (filter instanceof ExtensionFileFilter) exportFile = ((ExtensionFileFilter) filter).getCorrectFileName(exportFileChooser.getSelectedFile()); else exportFile = exportFileChooser.getSelectedFile();
                    if (!exportFile.createNewFile()) {
                        if (exportFile.canWrite()) {
                            confirm = JOptionPane.showConfirmDialog(parent, "<HTML><h3>A file already exists at this location.</h3>" + "<p>Shall I overwrite it?</p></html>", "Overwrite file", JOptionPane.OK_CANCEL_OPTION);
                        } else {
                            confirm = JOptionPane.showConfirmDialog(parent, "<HTML><h2>File already exists at this location.</h2>" + "<p>I cannot overwrite it.</p></html>", "File already exists", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }
                    if (confirm == JOptionPane.OK_OPTION) {
                        if (!exportFile.canWrite()) {
                            throw (new java.io.IOException("Cannot write to file"));
                        }
                        if (exportFile.getName().toLowerCase().endsWith("pdf")) {
                            PDFWriter pw = includeInterview ? new PDFWriter(currentStudy.second(), currentInterview.second()) : new PDFWriter(currentStudy.second());
                            pw.write(exportFile);
                        } else if (exportFile.getName().toLowerCase().endsWith("rtf")) {
                            RTFWriter pw = includeInterview ? new RTFWriter(currentStudy.second(), currentInterview.second()) : new RTFWriter(currentStudy.second());
                            pw.write(exportFile);
                        }
                        complete = true;
                    }
                } catch (java.io.IOException e) {
                    JOptionPane.showMessageDialog(parent, "Unable to write to file. File not saved.");
                    throw new RuntimeException(e);
                }
            } else {
                complete = true;
            }
        }
    }
