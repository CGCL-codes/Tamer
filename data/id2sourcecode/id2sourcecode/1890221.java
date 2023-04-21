    @Override
    public void actionPerformed(ActionEvent e) {
        if (algorithmFullNames.containsKey(e.getActionCommand())) {
            JCheckBox v = (JCheckBox) e.getSource();
            List<String> xx = apanel.getNames();
            if (v.isSelected()) {
                xx.add(v.getActionCommand());
            } else {
                if (xx.size() == 1) {
                    v.setSelected(true);
                    return;
                }
                xx.remove(e.getActionCommand());
            }
            linear.setEnabled(xx.size() > 1);
            weka.setEnabled(xx.size() > 1);
            svm.setEnabled(xx.size() > 1);
            apanel.setWeightButtons();
            return;
        }
        if (e.getActionCommand().equals("View")) {
            try {
                JDialog w = new JDialog(frame, "Configuratin", true);
                JScrollPane p = new JScrollPane(new JTextArea(factory.marshal(factory.createModule(definition()))));
                w.setContentPane(p);
                w.setPreferredSize(new Dimension(500, 300));
                w.pack();
                w.setVisible(true);
                return;
            } catch (Exception e1) {
                JOptionPane.showMessageDialog(frame, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        JFileChooser f = new JFileChooser(path);
        f.setMultiSelectionEnabled(false);
        f.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int returnVal = f.showSaveDialog(frame);
        if (returnVal != JFileChooser.APPROVE_OPTION) return;
        try {
            if (f.getSelectedFile().exists()) {
                if (JOptionPane.NO_OPTION == JOptionPane.showConfirmDialog(frame, "File alread exists. Do you want me to overwrite it")) return;
            }
            factory.marshal(f.getSelectedFile().getAbsolutePath(), factory.createModule(definition()), true);
            JOptionPane.showMessageDialog(frame, "Configuration Saved", "Done", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e1) {
            JOptionPane.showMessageDialog(frame, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        path = f.getSelectedFile().getParentFile();
    }
