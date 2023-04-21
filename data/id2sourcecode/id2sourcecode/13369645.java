        public void actionPerformed(ActionEvent e) {
            JFileChooser fc = new JFileChooser();
            File suggest = new File(GUI.getExternalToolsSetting("BUFFER_LISTENER_SAVEFILE", "BufferAccessLogger.txt"));
            fc.setSelectedFile(suggest);
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
            GUI.setExternalToolsSetting("BUFFER_LISTENER_SAVEFILE", saveFile.getPath());
            if (saveFile.exists() && !saveFile.canWrite()) {
                logger.fatal("No write access to file: " + saveFile);
                return;
            }
            try {
                PrintWriter outStream = new PrintWriter(new FileWriter(saveFile));
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < logTable.getRowCount(); i++) {
                    sb.append(logTable.getValueAt(i, COLUMN_TIME));
                    sb.append("\t");
                    sb.append(logTable.getValueAt(i, COLUMN_FROM));
                    sb.append("\t");
                    sb.append(logTable.getValueAt(i, COLUMN_TYPE));
                    sb.append("\t");
                    if (parser instanceof GraphicalParser) {
                        BufferAccess ba = (BufferAccess) logTable.getValueAt(i, COLUMN_DATA);
                        sb.append(ba.getAsHex());
                    } else {
                        sb.append(logTable.getValueAt(i, COLUMN_DATA));
                    }
                    sb.append("\t");
                    sb.append(logTable.getValueAt(i, COLUMN_SOURCE));
                    sb.append("\n");
                }
                outStream.print(sb.toString());
                outStream.close();
            } catch (Exception ex) {
                logger.fatal("Could not write to file: " + saveFile);
                return;
            }
        }
