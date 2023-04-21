    public static void writeFile(JFrame frame, String extensionDescription, String extension, String contents) {
        JFileChooser fc = new JFileChooser();
        fc.addChoosableFileFilter(new ExtensionFileFilter(extensionDescription, extension));
        fc.setAcceptAllFileFilterUsed(false);
        extension = extension.toLowerCase();
        int returnValue = fc.showSaveDialog(frame);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            String path = file.getPath();
            if (!path.toLowerCase().endsWith("." + extension)) {
                path += "." + extension;
                file = new File(path);
            }
            if (file.exists()) {
                System.out.println("file exists already");
                Object[] options = { "Overwrite file", "Cancel" };
                int choice = JOptionPane.showOptionDialog(frame, "The specified file already exists.  Overwrite existing file?", "Overwrite file?", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[1]);
                if (choice != 0) return;
            }
            try {
                BufferedWriter Writer = new BufferedWriter(new FileWriter(path));
                String[] lines = contents.split("\n");
                for (int i = 0; i < lines.length; ++i) {
                    Writer.write(lines[i]);
                    Writer.newLine();
                }
                Writer.close();
            } catch (IOException e) {
                showErrorDialog(frame, "Write file failed!");
            }
        }
    }
