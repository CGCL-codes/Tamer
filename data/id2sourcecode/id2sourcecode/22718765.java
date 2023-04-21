    private void savePDF() {
        MessageBox msgBox = new MessageBox(sShell, SWT.OK);
        if (bigListTitle.getCharCount() == 0) {
            msgBox.setMessage("The playlist needs a title!");
            msgBox.open();
            return;
        }
        FileDialog dialog = new FileDialog(sShell, SWT.SAVE);
        dialog.setText("Save PDF file");
        String[] filterExt = { ("*." + Constantsv1.PDFFileEnd) };
        dialog.setFilterExtensions(filterExt);
        dialog.setFileName(bigListTitle.getText());
        String result = dialog.open();
        if (result != null) {
            if (!result.toLowerCase().endsWith("." + Constantsv1.PDFFileEnd)) {
                result = result + "." + Constantsv1.PDFFileEnd;
            } else if (result.toLowerCase().endsWith(".")) result = result + Constantsv1.PDFFileEnd;
            File f = new File(result);
            msgBox = new MessageBox(sShell, SWT.YES | SWT.NO);
            msgBox.setMessage("The file already exists! Do you want to overwrite it?");
            try {
                if (!f.isFile() || msgBox.open() == SWT.YES) {
                    writeToPdf(f);
                } else {
                    savePDF();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
