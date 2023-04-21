    private void saveSelectedListActionPerformed(java.awt.event.ActionEvent evt) {
        ArrayList<CoordsList> list = new ArrayList<CoordsList>();
        for (int i = 0; i < listCoordsList.getSelectedIndices().length; i++) {
            list.add(coordsList.get(listCoordsList.getSelectedIndices()[i]));
        }
        if (!list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                File f = new File("SerializedCoords" + File.separator + list.get(i).name + ".ser");
                Boolean overWriteExistingSerFile = true;
                if (f.exists()) {
                    int response = 0;
                    try {
                        response = JOptionPane.showConfirmDialog(null, "Overwrite existing file(" + f.getCanonicalPath() + ")?", "The file already exists", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
                    } catch (IOException ex) {
                        Logger.getLogger(Graph.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    if (response == JOptionPane.CANCEL_OPTION) {
                        overWriteExistingSerFile = false;
                    }
                }
                if (overWriteExistingSerFile) {
                    try {
                        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f.getAbsolutePath()));
                        oos.writeObject(list.get(i));
                        oos.close();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
    }
