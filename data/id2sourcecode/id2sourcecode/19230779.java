    public Editor openEditor() {
        final Editor editor = new Editor();
        addNewFrame(editor);
        final SFileChooser chooser = new SFileChooser();
        chooser.setColumns(20);
        final SOptionPane dialog = new SOptionPane();
        dialog.setEncodingType("multipart/form-data");
        dialog.showInput(editor, "Choose file", chooser, "Open file");
        dialog.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                if (evt.getActionCommand() == SOptionPane.OK_ACTION) {
                    try {
                        File file = chooser.getFile();
                        Reader reader = new FileReader(file);
                        StringWriter writer = new StringWriter();
                        int b;
                        while ((b = reader.read()) >= 0) writer.write(b);
                        editor.setText(writer.toString());
                        editor.setTitle(chooser.getFileName());
                        chooser.reset();
                    } catch (Exception e) {
                        dialog.show(editor);
                        SOptionPane.showMessageDialog(editor, "Error opening file", e.getMessage());
                    }
                } else {
                    editor.close();
                }
            }
        });
        return editor;
    }
