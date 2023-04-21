    private void setWriteMode(InsertOverwriteTextPane.TypingMode mode) {
        if (mode == InsertOverwriteTextPane.TypingMode.INSERT) {
            insertOverwriteLabel.setText(PropertyManager.readProperty("insert.label.ins"));
        } else {
            insertOverwriteLabel.setText(PropertyManager.readProperty("insert.label.ovr"));
        }
    }
