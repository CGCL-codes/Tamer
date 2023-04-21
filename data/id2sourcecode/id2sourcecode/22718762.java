    private void saveFile() {
        FileDialog dialog = new FileDialog(sShell, SWT.SAVE);
        dialog.setText("Save file");
        dialog.setFileName(bigListTitle.getText());
        String[] filterExt = { ("*." + Constantsv1.FileEnd) };
        dialog.setFilterExtensions(filterExt);
        String result = dialog.open();
        if (result != null) {
            if (!result.toLowerCase().endsWith("." + Constantsv1.FileEnd)) {
                result = result + "." + Constantsv1.FileEnd;
            } else if (result.toLowerCase().endsWith(".")) result = result + Constantsv1.FileEnd;
            File f = new File(result);
            MessageBox msgBox = new MessageBox(sShell, SWT.YES | SWT.NO);
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
