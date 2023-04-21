    public int getCheckBoxSelected() {
        if (readCheckBox.isSelected() && writeCheckBox.isSelected()) {
            return 3;
        } else {
            if (readCheckBox.isSelected()) {
                return 1;
            }
            if (writeCheckBox.isSelected()) {
                return 2;
            }
        }
        return 0;
    }
