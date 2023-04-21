    public void addText(String s) {
        int i;
        for (i = 0; i < lines.length - 1; i++) lines[i] = lines[i + 1];
        lines[i] = s;
        String tmp = "";
        for (i = 0; i < lines.length; i++) tmp += lines[i] + "\n";
        textArea.setText(tmp);
        textArea.refresh();
    }
