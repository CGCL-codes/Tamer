    private void saveFile() {
        MessageBox msgBox = new MessageBox(sShell, SWT.OK);
        if (tableBigPlaylist.getItemCount() == 0) {
            if (tableDay01.getItemCount() == 0 && tableDay02.getItemCount() == 0 && tableDay03.getItemCount() == 0 && tableDay04.getItemCount() == 0 && tableDay05.getItemCount() == 0 && tableDay06.getItemCount() == 0 && tableDay07.getItemCount() == 0 && tableDay08.getItemCount() == 0 && tableDay09.getItemCount() == 0 && tableDay10.getItemCount() == 0 && tableDay11.getItemCount() == 0 && tableDay12.getItemCount() == 0 && tableDay13.getItemCount() == 0 && tableDay14.getItemCount() == 0 && tableDay15.getItemCount() == 0) {
                msgBox.setMessage("All playlists are empty!");
                msgBox.open();
                return;
            }
        }
        FileDialog dialog = new FileDialog(sShell, SWT.SAVE);
        dialog.setText("Save file");
        if (tabFolder.getSelectionIndex() == 1) dialog.setFileName(textTitle15.getText()); else dialog.setFileName(bigListTitle.getText());
        String[] filterExt = { ("*." + Constantsv1.FileEnd) };
        dialog.setFilterExtensions(filterExt);
        String result = dialog.open();
        if (result != null) {
            if (!result.toLowerCase().endsWith("." + Constantsv1.FileEnd)) {
                result = result + "." + Constantsv1.FileEnd;
            } else if (result.toLowerCase().endsWith(".")) result = result + Constantsv1.FileEnd;
            File f = new File(result);
            msgBox = new MessageBox(sShell, SWT.YES | SWT.NO);
            msgBox.setMessage("The file already exists! Do you want to overwrite it?");
            try {
                if (!f.isFile() || msgBox.open() == SWT.YES) {
                    wirteToFile(f);
                    strTitle = f.getName();
                    setChange(false);
                } else {
                    saveFile();
                }
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
