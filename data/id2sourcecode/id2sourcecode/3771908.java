    @Override
    public void setupDisplay(JPanel panel, final CDOMObject pc) {
        panel.setLayout(layout);
        JLabel label = new JLabel("Please select the Directory where Converted files should be written: ");
        JButton button = new JButton("Browse...");
        button.setMnemonic('r');
        button.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                JFileChooser chooser = new JFileChooser();
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                chooser.setDialogType(JFileChooser.OPEN_DIALOG);
                chooser.setSelectedFile(path);
                while (true) {
                    int open = chooser.showOpenDialog(null);
                    if (open == JFileChooser.APPROVE_OPTION) {
                        File fileToOpen = chooser.getSelectedFile();
                        if (fileToOpen.isDirectory() && fileToOpen.canRead() && fileToOpen.canWrite()) {
                            path = fileToOpen;
                            pc.put(ObjectKey.WRITE_DIRECTORY, path);
                            fileLabel.setText(path.getAbsolutePath());
                            break;
                        }
                        JOptionPane.showMessageDialog(null, "Selection must be a valid " + "(readable & writeable) Directory");
                        chooser.setSelectedFile(path);
                    } else if (open == JFileChooser.CANCEL_OPTION) {
                        break;
                    }
                }
            }
        });
        panel.add(label);
        panel.add(fileLabel);
        panel.add(button);
        layout.putConstraint(SpringLayout.NORTH, label, 50, SpringLayout.NORTH, panel);
        layout.putConstraint(SpringLayout.NORTH, fileLabel, 75 + label.getPreferredSize().height, SpringLayout.NORTH, panel);
        layout.putConstraint(SpringLayout.NORTH, button, 75 + label.getPreferredSize().height, SpringLayout.NORTH, panel);
        layout.putConstraint(SpringLayout.WEST, label, 25, SpringLayout.WEST, panel);
        layout.putConstraint(SpringLayout.WEST, fileLabel, 25, SpringLayout.WEST, panel);
        layout.putConstraint(SpringLayout.EAST, button, -50, SpringLayout.EAST, panel);
        fileLabel.setText(path.getAbsolutePath());
    }
