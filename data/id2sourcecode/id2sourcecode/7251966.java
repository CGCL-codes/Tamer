    private JButton getUpdatePrefsBtn() {
        if (updatePrefsBtn == null) {
            updatePrefsBtn = new JButton();
            updatePrefsBtn.setBounds(new Rectangle(10, 135, 165, 23));
            updatePrefsBtn.setText("Update Preferences");
            updatePrefsBtn.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    page.setDelayAmount(getDelayTF().getText());
                    page.setCCOffset(getCcOffsetTF().getText());
                    page.setMidiChannel(getChannelTF().getText());
                    page.setHorizontal(getHorizontalCB().isSelected());
                    page.redrawDevice();
                }
            });
        }
        return updatePrefsBtn;
    }
