    public void actionPerformed(ZElement el) {
        ZFile c = (ZFile) el;
        File f = c.getFile();
        String n = f.getName();
        n = n.substring(0, n.lastIndexOf('.')) + ".zl";
        f = new File(f.getParent(), n);
        if (f.exists()) {
            int ret = JOptionPane.showConfirmDialog(MainFrame.getInstance(), "File " + f + " already exists.\n" + "Would you like to overwrite it?", "Overwrite?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (ret == JOptionPane.NO_OPTION) return;
        }
        try {
            IndentedWriter w = new IndentedWriter(new BufferedWriter(new FileWriter(f)));
            XMLSerializer xmls = new XMLSerializer(w);
            xmls.setPublicID(ZFile.PUBLIC_ID);
            xmls.setSystemID("zlang.dtd");
            c.printXML(new XMLCreator(xmls));
            w.close();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(MainFrame.getInstance(), "Error writing file " + f + " " + e.getMessage());
        }
    }
