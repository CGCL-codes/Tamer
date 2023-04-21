    public void updateInputString() {
        s = textPunchField.getText().split("\n");
        channel.setStringArray(s);
        channel.setText(s);
        channel.getChannelDisplay().repaint();
        channel.getOutputPreviewPanel().repaint();
        channel.getOutputDisplayPanel().repaint();
    }
