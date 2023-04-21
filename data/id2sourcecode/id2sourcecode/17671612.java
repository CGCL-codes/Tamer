    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {
        String text = givensTextArea.getText();
        String lineEnd = null;
        if (text.contains("\r\n")) {
            lineEnd = "\r\n";
        } else if (text.contains("\r")) {
            lineEnd = "\r";
        } else if (text.contains("\n")) {
            lineEnd = "\n";
        }
        String[] lines = text.split(text);
        if (lineEnd != null) {
            lines = text.split(lineEnd);
        } else {
            lines = new String[1];
            lines[0] = text;
        }
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < lines.length; i++) {
            if (lines[i] != null) {
                lines[i] = lines[i].trim();
                if (lines[i].contains("---")) {
                    for (int j = i; j < lines.length - 1; j++) {
                        lines[j] = lines[j + 1];
                    }
                    lines[lines.length - 1] = null;
                }
            }
            if (lines[i] != null) {
                StringBuffer tmp = new StringBuffer(lines[i].trim());
                for (int j = 0; j < tmp.length(); j++) {
                    char ch = tmp.charAt(j);
                    if (!Character.isDigit(ch) && ch != '.') {
                        tmp.deleteCharAt(j);
                        j--;
                    }
                }
                output.append(tmp);
            }
        }
        givens = output.toString();
        if (givens.length() != 81) {
            JOptionPane.showMessageDialog(rootPane, ResourceBundle.getBundle("intl/SetGivensDialog").getString("SetGivensDialog.error.message"), ResourceBundle.getBundle("intl/SetGivensDialog").getString("SetGivensDialog.error.title"), JOptionPane.ERROR_MESSAGE);
            givens = null;
            return;
        }
        okPressed = true;
        setVisible(false);
    }
