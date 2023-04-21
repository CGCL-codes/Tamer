    private void savePDF() {
        MessageBox msgBox = new MessageBox(sShell, SWT.OK);
        if ((bigListTitle.getCharCount() == 0 && tabFolder.getSelectionIndex() == 0) || (tabFolder.getSelectionIndex() == 1 && textTitle15.getCharCount() == 0)) {
            msgBox.setMessage("The playlist needs a title!");
            msgBox.open();
            return;
        }
        if (tabFolder.getSelectionIndex() == 0) if (tableBigPlaylist.getItemCount() == 0) {
            msgBox.setMessage("The playlist is empty!");
            msgBox.open();
            return;
        }
        if (tabFolder.getSelectionIndex() == 1) if (tableDay01.getItemCount() == 0 && tableDay02.getItemCount() == 0 && tableDay03.getItemCount() == 0 && tableDay04.getItemCount() == 0 && tableDay05.getItemCount() == 0 && tableDay06.getItemCount() == 0 && tableDay07.getItemCount() == 0 && tableDay08.getItemCount() == 0 && tableDay09.getItemCount() == 0 && tableDay10.getItemCount() == 0 && tableDay11.getItemCount() == 0 && tableDay12.getItemCount() == 0 && tableDay13.getItemCount() == 0 && tableDay14.getItemCount() == 0 && tableDay15.getItemCount() == 0) {
            msgBox.setMessage("All playlists are empty!");
            msgBox.open();
            return;
        }
        FileDialog dialog = new FileDialog(sShell, SWT.SAVE);
        dialog.setText("Save PDF file");
        String[] filterExt = { ("*." + Constantsv1.PDFFileEnd) };
        dialog.setFilterExtensions(filterExt);
        if (tabFolder.getSelectionIndex() == 1) dialog.setFileName(textTitle15.getText()); else dialog.setFileName(bigListTitle.getText());
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
