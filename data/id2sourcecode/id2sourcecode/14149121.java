    public void saveChart() {
        JFileChooser fc = new JFileChooser("./samples/");
        int rc = fc.showDialog(null, "Save");
        if (rc == JFileChooser.APPROVE_OPTION) {
            File f = fc.getSelectedFile();
            if (f.exists()) {
                Object[] options = { "OK", "Cancel" };
                if (JOptionPane.showOptionDialog(UI, "File already exists: " + f.getName() + "\nDo you want to overwrite it?", "Warning!", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[1]) == 1) {
                    return;
                }
            }
            String filepath = f.getAbsoluteFile().toString();
            String extention = ".xml";
            if (filepath.toLowerCase().endsWith(extention)) {
                writer.SaveXML(filepath);
            } else {
                writer.SaveXML(filepath + extention);
            }
        } else {
            System.out.println("Error: file was not saved!");
        }
        linkChart.setDataChanged(false);
        return;
    }
