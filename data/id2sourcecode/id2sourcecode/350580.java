    public void WriteProjProperties() {
        File home = new File(System.getProperty("user.home") + "/.dvd-homevideo/properties");
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(home));
            writer.write("<dvd-homevideo properties>");
            writer.newLine();
            writer.write("\t<JRadioButtonMenuItem>");
            writer.newLine();
            writer.write("\t\t<menuRdNTSC>");
            writer.newLine();
            writer.write("\t\t\t<selected>" + menuRdNTSC.isSelected() + "</selected>");
            writer.newLine();
            writer.write("\t\t</menuRdNTSC>");
            writer.newLine();
            writer.write("\t\t<menuRd_4_3>");
            writer.newLine();
            writer.write("\t\t\t<selected>" + menuRd_4_3.isSelected() + "</selected>");
            writer.newLine();
            writer.write("\t\t</menuRd_4_3>");
            writer.newLine();
            writer.write("\t</JRadioButonMenuItem>");
            writer.newLine();
            writer.write("\t<JCheckBoxMenuItem>");
            writer.newLine();
            writer.write("\t\t<menuChkThread>");
            writer.newLine();
            writer.write("\t\t\t<selected>" + menuChkThread.isSelected() + "</selected>");
            writer.newLine();
            writer.write("\t\t</menuChkThread>");
            writer.newLine();
            writer.write("\t</JCheckBoxMenuItem>");
            writer.newLine();
            writer.close();
            rdNTSC.setSelected(menuRdNTSC.isSelected());
            rdPAL.setSelected(menuRdPAL.isSelected());
            rd4_3.setSelected(menuRd_4_3.isSelected());
            rd16_9.setSelected(menuRd_16_9.isSelected());
        } catch (IOException ex) {
            SaveStackTrace.printTrace(strOutputDir, ex);
            MessageBox("IO Error in WriteProjectProperties in GUI.java\n" + ex.toString(), 0);
            ex.printStackTrace();
        }
    }
