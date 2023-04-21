    private JRadioButton getReadWriteRadioButton() {
        if (readWriteRadioButton == null) {
            readWriteRadioButton = new JRadioButton();
            readWriteRadioButton.setText(ResourceUtil.getString("accesslevel.readwrite"));
            readWriteRadioButton.setFont(GuiConstants.FONT_PLAIN);
            if (accessRule != null && accessRule.getLevel().equals(SubversionConstants.SVN_ACCESS_LEVEL_READWRITE)) {
                readWriteRadioButton.setSelected(true);
            }
        }
        return readWriteRadioButton;
    }
