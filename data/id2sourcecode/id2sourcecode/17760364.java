    public final void doExport() {
        FileDialog inDialog = new FileDialog(this, "Import Tree File...", FileDialog.LOAD);
        inDialog.setVisible(true);
        if (inDialog.getFile() != null) {
            File inFile = new File(inDialog.getDirectory(), inDialog.getFile());
            FileDialog outDialog = new FileDialog(this, "Save Log File As...", FileDialog.SAVE);
            outDialog.setVisible(true);
            if (outDialog.getFile() != null) {
                File outFile = new File(outDialog.getDirectory(), outDialog.getFile());
                try {
                    processTreeFile(inFile, outFile);
                } catch (FileNotFoundException fnfe) {
                    JOptionPane.showMessageDialog(this, "Unable to open file: File not found", "Unable to open file", JOptionPane.ERROR_MESSAGE);
                } catch (IOException ioe) {
                    JOptionPane.showMessageDialog(this, "Unable to read/write file: " + ioe, "Unable to read/write file", JOptionPane.ERROR_MESSAGE);
                } catch (Importer.ImportException ie) {
                    JOptionPane.showMessageDialog(this, "Unable to import file: " + ie, "Unable to import tree file", JOptionPane.ERROR_MESSAGE);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Error: " + e, "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
