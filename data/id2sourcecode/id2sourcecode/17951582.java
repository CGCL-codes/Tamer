        HD(Emulator.Architecture.Modules.Ata.Harddiskdrive hd) {
            this.hd = hd;
            enabled.setSelected(hd.isEnabled());
            channelIndex.setText(hd.getChannelindex().toString());
            master.setSelected(hd.isMaster());
            autoDetect.setSelected(hd.isAutodetectcylinders());
            cylinders.setText(hd.getCylinders().toString());
            heads.setText(hd.getHeads().toString());
            sectors.setText(hd.getSectorspertrack().toString());
            imageFile.setText(hd.getImagefilepath());
            imageFile.setEditable(false);
            imageFile.setToolTipText(imageFile.getText());
            imageFile.addFocusListener(new FocusAdapter() {

                @Override
                public void focusGained(FocusEvent e) {
                    imageFile.setCaretPosition(imageFile.getText().length());
                }
            });
            super.setLayout(new GridLayout(0, 3, 5, 5));
            super.add(new JLabel("Enabled"));
            super.add(enabled);
            super.add(new JLabel());
            super.add(new JLabel("Channel index"));
            super.add(channelIndex);
            super.add(new JLabel());
            super.add(new JLabel("Master"));
            super.add(master);
            super.add(new JLabel());
            super.add(new JLabel("Auto detect"));
            super.add(autoDetect);
            super.add(new JLabel());
            super.add(new JLabel("Cylinders"));
            super.add(cylinders);
            super.add(new JLabel());
            super.add(new JLabel("Heads"));
            super.add(heads);
            super.add(new JLabel());
            super.add(new JLabel("Sectors"));
            super.add(sectors);
            super.add(new JLabel());
            super.add(new JLabel("Image file"));
            super.add(imageFile);
            super.add(browse);
            browse.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    File file = AtaPanel.super.chooseFile();
                    if (file != null) {
                        imageFile.setText(file.getAbsolutePath());
                        imageFile.setToolTipText(imageFile.getText());
                    }
                }
            });
        }
