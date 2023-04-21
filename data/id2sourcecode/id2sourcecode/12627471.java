    private void saveIt() {
        JFileChooser chooser = new JFileChooser();
        chooser.addChoosableFileFilter(new FisFileFilter(OLD_suff, OLD_desc));
        chooser.addChoosableFileFilter(new FisFileFilter(XML_suff, XML_desc));
        int r = chooser.showSaveDialog(this);
        if (r == JFileChooser.APPROVE_OPTION) {
            String t = chooser.getSelectedFile().getAbsolutePath();
            File f = new File(t);
            boolean overwrite = true;
            if (f.exists()) {
                int ans = JOptionPane.showConfirmDialog(this, "The file already exists, overwrite?", "Are you sure?", JOptionPane.YES_NO_OPTION);
                if (ans == JOptionPane.YES_OPTION) overwrite = false;
            }
            if (overwrite) {
                if (chooser.getFileFilter().getDescription().equals(XML_desc)) {
                    if (!t.endsWith(XML_suff)) t = t + XML_suff;
                    FisBase[] bases = new FisBase[fisbases.size()];
                    for (int i = 0; i < bases.length; i++) bases[i] = (FisBase) fisbases.elementAt(i);
                    XmlSerializationFactory sf = new XmlSerializationFactory(this, t);
                    try {
                        sf.saveWizard(bases);
                        sf = null;
                    } catch (Exception ex) {
                        Dialogs.ShowErrorDialog(this, "Error saving file!");
                    }
                } else {
                    SerializationFactory sf = new SerializationFactory(t);
                    try {
                        sf.saveWizard(fisbases);
                        sf = null;
                    } catch (Exception ex) {
                        Dialogs.ShowErrorDialog(this, "Error saving file!");
                    }
                }
            }
        }
    }
