        public void actionPerformed(ActionEvent e) {
            JFileChooser fc = new JFileChooser();
            int returnVal = fc.showSaveDialog(GUI.getTopParentContainer());
            if (returnVal != JFileChooser.APPROVE_OPTION) {
                return;
            }
            File saveFile = fc.getSelectedFile();
            if (saveFile.exists()) {
                String s1 = "Overwrite";
                String s2 = "Cancel";
                Object[] options = { s1, s2 };
                int n = JOptionPane.showOptionDialog(GUI.getTopParentContainer(), "A file with the same name already exists.\nDo you want to remove it?", "Overwrite existing file?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, s1);
                if (n != JOptionPane.YES_OPTION) {
                    return;
                }
            }
            if (saveFile.exists() && !saveFile.canWrite()) {
                logger.fatal("No write access to file: " + saveFile);
                return;
            }
            try {
                PrintWriter outStream = new PrintWriter(new FileWriter(saveFile));
                for (int i = 0; i < connections.size(); i++) {
                    outStream.print("" + dataTable.getValueAt(i, COLUMN_TIME) + '\t');
                    outStream.print("" + dataTable.getValueAt(i, COLUMN_FROM) + '\t');
                    outStream.print("" + getDestString(connections.get(i)) + '\t');
                    outStream.print("" + dataTable.getValueAt(i, COLUMN_DATA) + '\n');
                }
                outStream.close();
            } catch (Exception ex) {
                logger.fatal("Could not write to file: " + saveFile);
                return;
            }
        }
