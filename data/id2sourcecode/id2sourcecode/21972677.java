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
