    public void actionPerformed(ZElement el) {
        ZFile c = (ZFile) el;
        File f = c.getFile();
        String n = f.getName();
        n = n.substring(0, n.lastIndexOf('.')) + ".java";
        f = new File(f.getParent(), n);
        if (f.exists()) {
            int ret = JOptionPane.showConfirmDialog(MainFrame.getInstance(), "File " + f + " already exists.\n" + "Would you like to overwrite it?", "Overwrite?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (ret == JOptionPane.NO_OPTION) return;
        }
        try {
            IndentedWriter w = new IndentedWriter(new BufferedWriter(new FileWriter(f)));
            c.printJava(w);
            w.close();
        } catch (UnsupportedOperationException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(MainFrame.getInstance(), "Syntax not yet supported " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(MainFrame.getInstance(), "Error writing file " + f + " " + e.getMessage());
        }
    }
