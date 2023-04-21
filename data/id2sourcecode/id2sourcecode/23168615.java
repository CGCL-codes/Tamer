    public synchronized void fileSaveAs(boolean as) {
        FileDialog d = null;
        File file = null;
        int op, inSize, outSize;
        FileOutputStream out;
        Enumeration e;
        if (as || fileName == null) {
            d = new FileDialog(jparent, "Save As...", FileDialog.SAVE);
            d.setFile("*.jst");
            d.setDirectory(".");
            d.setVisible(true);
            fileName = d.getFile();
            if (fileName == null) return;
        }
        file = new File(fileName);
        if (file.exists()) {
            op = JOptionPane.showConfirmDialog(jparent, "File " + fileName + "already exists.  Overwrite?", "File exists", JOptionPane.YES_NO_OPTION);
            if (op == JOptionPane.NO_OPTION) return;
        }
        try {
            out = new FileOutputStream(fileName);
            inSize = inTable.size();
            outSize = inTable.size();
            if (this instanceof Transmitter) out.write((int) 't'); else out.write((int) 'r');
            SMPPPacket.writeInt(outSize, 4, out);
            e = outTable.elements();
            while (e.hasMoreElements()) {
                ((SMPPPacket) e.nextElement()).writeTo(out);
            }
            SMPPPacket.writeInt(inSize, 4, out);
            e = inTable.elements();
            while (e.hasMoreElements()) {
                ((SMPPPacket) e.nextElement()).writeTo(out);
            }
            out.close();
            JOptionPane.showMessageDialog(jparent, "File saved successfully.", "File save", JOptionPane.PLAIN_MESSAGE);
        } catch (IOException ix) {
            JOptionPane.showMessageDialog(jparent, ix.getMessage(), "Save error", JOptionPane.ERROR_MESSAGE);
        }
    }
