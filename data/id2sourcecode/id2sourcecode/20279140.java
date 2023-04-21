    @Override
    public void run() {
        if (!templateRoot.exists()) {
            templateRoot.mkdir();
            templateRoot.setExecutable(true);
            templateRoot.setReadable(true);
            templateRoot.setWritable(true);
            System.err.println("templates folder was created");
        }
        FileDialog saveTemplate = new FileDialog(editor, "SaveTemplate from sketch", FileDialog.SAVE);
        saveTemplate.setDirectory(templateRoot.getAbsolutePath());
        saveTemplate.setFile("new_template.txt");
        saveTemplate.setVisible(true);
        String templateName = saveTemplate.getFile();
        if (null == templateName) {
            this.editor.statusNotice("Saving template file cancelled...");
            return;
        }
        if (!templateName.endsWith("txt") && !templateName.endsWith("TXT")) {
            templateName += ".txt";
        }
        File theTemplate = new File(templateRoot.getAbsolutePath() + fs + templateName);
        int response = 0;
        if (theTemplate.exists()) {
            response = JOptionPane.showConfirmDialog(editor.getContentPane(), (Object) "The file " + theTemplate.getName() + " already exixts in this folder, do you want to overwrite it?", toolName + toolVersion, JOptionPane.OK_CANCEL_OPTION);
        }
        if (2 != response) {
            String templateContents = this.editor.getText();
            try {
                PrintWriter templateFile = new PrintWriter(theTemplate);
                templateFile.print(templateContents);
                templateFile.flush();
                templateFile.close();
                this.editor.statusNotice("Template saved as \"" + theTemplate.getName() + "\".");
                System.out.println("Success! - You can find your new template in:\n" + templateRoot.getAbsolutePath());
                System.out.println(String.format("%s %s", toolName, toolVersion));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            this.editor.statusNotice("Saving template file cancelled...");
        }
    }
