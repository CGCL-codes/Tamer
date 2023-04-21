    public void run() {
        JFileChooser fc = new JFileChooser(jmri.jmrit.XmlFile.userFileLocationDefault());
        fc.addChoosableFileFilter(new textFilter());
        File fs = new File("NCE macro backup.txt");
        fc.setSelectedFile(fs);
        int retVal = fc.showSaveDialog(null);
        if (retVal != JFileChooser.APPROVE_OPTION) return;
        if (fc.getSelectedFile() == null) return;
        File f = fc.getSelectedFile();
        if (fc.getFileFilter() != fc.getAcceptAllFileFilter()) {
            String fileName = f.getAbsolutePath();
            String fileNameLC = fileName.toLowerCase();
            if (!fileNameLC.endsWith(".txt")) {
                fileName = fileName + ".txt";
                f = new File(fileName);
            }
        }
        if (f.exists()) {
            if (JOptionPane.showConfirmDialog(null, "File " + f.getName() + " already exists, overwrite it?", "Overwrite file?", JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) {
                return;
            }
        }
        PrintWriter fileOut;
        try {
            fileOut = new PrintWriter(new BufferedWriter(new FileWriter(f)), true);
        } catch (IOException e) {
            return;
        }
        if (JOptionPane.showConfirmDialog(null, "Backup can take over a minute, continue?", "NCE Macro Backup", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
            fileOut.close();
            return;
        }
        JPanel ps = new JPanel();
        jmri.util.JmriJFrame fstatus = new jmri.util.JmriJFrame("Macro Backup");
        fstatus.setLocationRelativeTo(null);
        fstatus.setSize(200, 100);
        fstatus.getContentPane().add(ps);
        ps.add(textMacro);
        ps.add(macroNumber);
        textMacro.setText("Macro number:");
        textMacro.setVisible(true);
        macroNumber.setVisible(true);
        waiting = 0;
        fileValid = true;
        for (int macroNum = 0; macroNum < NUM_MACRO; macroNum++) {
            macroNumber.setText(Integer.toString(macroNum));
            fstatus.setVisible(true);
            getNceMacro(macroNum);
            if (!fileValid) macroNum = NUM_MACRO;
            if (fileValid) {
                StringBuffer buf = new StringBuffer();
                buf.append(":" + Integer.toHexString(CS_MACRO_MEM + (macroNum * MACRO_LNTH)));
                for (int i = 0; i < MACRO_LNTH; i++) {
                    buf.append(" " + StringUtil.twoHexFromInt(nceMacroData[i++]));
                    buf.append(StringUtil.twoHexFromInt(nceMacroData[i]));
                }
                if (log.isDebugEnabled()) log.debug("macro " + buf.toString());
                fileOut.println(buf.toString());
            }
        }
        if (fileValid) {
            String line = ":0000";
            fileOut.println(line);
        }
        fileOut.flush();
        fileOut.close();
        fstatus.dispose();
        if (fileValid) {
            JOptionPane.showMessageDialog(null, "Successful Backup!", "NCE Macro", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Backup failed", "NCE Macro", JOptionPane.ERROR_MESSAGE);
        }
    }
