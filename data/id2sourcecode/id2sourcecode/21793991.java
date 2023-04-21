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
                logger.fatal("No write access to file");
                return;
            }
            try {
                BufferedWriter outStream = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(saveFile)));
                for (MoteEvents moteEvents : allMoteEvents) {
                    for (MoteEvent ev : moteEvents.ledEvents) {
                        outStream.write(moteEvents.mote + "\t" + ev.time + "\t" + ev.toString() + "\n");
                    }
                    for (MoteEvent ev : moteEvents.logEvents) {
                        outStream.write(moteEvents.mote + "\t" + ev.time + "\t" + ev.toString() + "\n");
                    }
                    for (MoteEvent ev : moteEvents.radioChannelEvents) {
                        outStream.write(moteEvents.mote + "\t" + ev.time + "\t" + ev.toString() + "\n");
                    }
                    for (MoteEvent ev : moteEvents.radioHWEvents) {
                        outStream.write(moteEvents.mote + "\t" + ev.time + "\t" + ev.toString() + "\n");
                    }
                    for (MoteEvent ev : moteEvents.radioRXTXEvents) {
                        outStream.write(moteEvents.mote + "\t" + ev.time + "\t" + ev.toString() + "\n");
                    }
                    for (MoteEvent ev : moteEvents.watchpointEvents) {
                        outStream.write(moteEvents.mote + "\t" + ev.time + "\t" + ev.toString() + "\n");
                    }
                }
                outStream.close();
            } catch (Exception ex) {
                logger.fatal("Could not write to file: " + saveFile);
                return;
            }
        }
