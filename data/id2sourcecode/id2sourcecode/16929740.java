    private void loadListActionPerformed(java.awt.event.ActionEvent evt) {
        CoordsList newList;
        Coords newCoords;
        int returnVal = loadListFileChooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            String loadListName;
            for (int i = 0; i < loadListFileChooser.getSelectedFiles().length; i++) {
                if (loadListFileChooser.getSelectedFiles()[i].getAbsolutePath().endsWith(".ser")) {
                    loadListName = loadListFileChooser.getSelectedFiles()[i].getPath().split("\\\\")[(loadListFileChooser.getSelectedFiles()[i].getPath().split("\\\\").length) - 1].substring(0, loadListFileChooser.getSelectedFiles()[i].getPath().split("\\\\")[(loadListFileChooser.getSelectedFiles()[i].getPath().split("\\\\").length) - 1].length() - 4);
                    boolean nameEquals = false, overWriteEnabled = true;
                    for (int j = 0; j < coordsList.size(); j++) {
                        if (loadListName.equals(coordsList.get(j).name)) {
                            nameEquals = true;
                        }
                    }
                    if (nameEquals) {
                        int response = 0;
                        response = JOptionPane.showConfirmDialog(null, "Overwrite existing file(" + loadListName + ")?", "The file already exists", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
                        if (response == JOptionPane.CANCEL_OPTION) {
                            overWriteEnabled = false;
                        }
                    }
                    if (overWriteEnabled) {
                        try {
                            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(loadListFileChooser.getSelectedFiles()[i]));
                            newList = (CoordsList) ois.readObject();
                            ois.close();
                            for (int j = 0; j < coordsList.size(); j++) {
                                if (loadListName.equals(coordsList.get(j).name)) {
                                    coordsList.set(j, newList);
                                }
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                    if (!nameEquals) {
                        try {
                            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(loadListFileChooser.getSelectedFiles()[i]));
                            newList = (CoordsList) ois.readObject();
                            ois.close();
                            coordsList.add(newList);
                            DefaultListModel lm = (DefaultListModel) listCoordsList.getModel();
                            lm.addElement(coordsList.get(coordsList.size() - 1).name);
                            listCoordsList.setModel(lm);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
                if (loadListFileChooser.getSelectedFiles()[i].getAbsolutePath().endsWith(".txt")) {
                    try {
                        ArrayList<Coords> txtCoords = new ArrayList<Coords>();
                        BufferedReader br = new BufferedReader(new FileReader(loadListFileChooser.getSelectedFiles()[i]));
                        String txtListName = br.readLine();
                        while (br.ready()) {
                            String tmpLine = br.readLine();
                            txtCoords.add(new Coords(Double.parseDouble(tmpLine.split(" ")[0]), Double.parseDouble(tmpLine.split(" ")[1])));
                        }
                        br.close();
                        boolean overWriteEnabled = true, nameEquals = false;
                        for (int j = 0; j < coordsList.size(); j++) {
                            if (txtListName.equals(coordsList.get(j).name)) {
                                nameEquals = true;
                            }
                        }
                        if (nameEquals) {
                            int response = 0;
                            response = JOptionPane.showConfirmDialog(null, "Overwrite existing file(" + txtListName + ")?", "The list already exists!", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
                            if (response == JOptionPane.CANCEL_OPTION) {
                                overWriteEnabled = false;
                            }
                        }
                        if (overWriteEnabled) {
                            for (int j = 0; j < coordsList.size(); j++) {
                                if (txtListName.equals(coordsList.get(j).name)) {
                                    coordsList.set(j, new CoordsList(txtListName, txtCoords));
                                }
                            }
                        }
                        if (!nameEquals) {
                            coordsList.add(new CoordsList(txtListName, txtCoords));
                        }
                        ;
                        DefaultListModel lm = new DefaultListModel();
                        for (int j = 0; j < coordsList.size(); j++) {
                            if (!coordsList.get(j).coords.isEmpty()) {
                                lm.addElement(coordsList.get(j).name);
                            }
                        }
                        listCoordsList.setModel(lm);
                    } catch (Exception ex) {
                        Logger.getLogger(Graph.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    }
