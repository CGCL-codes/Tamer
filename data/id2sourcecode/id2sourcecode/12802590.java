    public void run() {
        JFileChooser fc = new JFileChooser(jmri.jmrit.XmlFile.userFileLocationDefault());
        fc.addChoosableFileFilter(new textFilter());
        File fs = new File("NCE consist backup.txt");
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
        if (JOptionPane.showConfirmDialog(null, "Backup can take over a minute, continue?", "NCE Consist Backup", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
            fileOut.close();
            return;
        }
        JPanel ps = new JPanel();
        jmri.util.JmriJFrame fstatus = new jmri.util.JmriJFrame("Consist Backup");
        fstatus.setLocationRelativeTo(null);
        fstatus.setSize(200, 100);
        fstatus.getContentPane().add(ps);
        ps.add(textConsist);
        ps.add(consistNumber);
        textConsist.setText("Consist line number:");
        textConsist.setVisible(true);
        consistNumber.setVisible(true);
        waiting = 0;
        fileValid = true;
        for (int consistNum = 0; consistNum < NUM_CONSIST; consistNum++) {
            consistNumber.setText(Integer.toString(consistNum));
            fstatus.setVisible(true);
            getNceConsist(consistNum);
            if (!fileValid) consistNum = NUM_CONSIST;
            if (fileValid) {
                StringBuffer buf = new StringBuffer();
                buf.append(":" + Integer.toHexString(CS_CONSIST_MEM + (consistNum * CONSIST_LNTH)));
                for (int i = 0; i < CONSIST_LNTH; i++) {
                    buf.append(" " + StringUtil.twoHexFromInt(nceConsistData[i++]));
                    buf.append(StringUtil.twoHexFromInt(nceConsistData[i]));
                }
                if (log.isDebugEnabled()) log.debug("consist " + buf.toString());
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
            JOptionPane.showMessageDialog(null, "Successful Backup!", "NCE Consist", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Backup failed", "NCE Consist", JOptionPane.ERROR_MESSAGE);
        }
    }
