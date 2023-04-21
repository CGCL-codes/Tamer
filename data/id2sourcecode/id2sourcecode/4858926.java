    public void actionPerformed(ActionEvent evt) {
        try {
            URL url = root.panel.view.getURL();
            if (url != null) {
                if (url.toString().startsWith("file:")) {
                    String str = url.toString();
                    str = str.substring(6, str.length() - 6);
                    if (new File(str + ".diff").exists()) {
                        int n = JOptionPane.showConfirmDialog(root.panel.view, "Diff already exists. Overwrite?", "Warning", JOptionPane.OK_CANCEL_OPTION);
                        if (n != JOptionPane.OK_OPTION) {
                            return;
                        }
                    }
                    DocumentDiffTest.generateTestFile(str + ".xhtml", str + ".diff", 500, 500);
                    Uu.p("wrote out: " + str + ".diff");
                }
            }
        } catch (Exception ex) {
            Uu.p(ex);
        }
    }
