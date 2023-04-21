    private boolean saveFile(File f, FileType ft) {
        checkValidity();
        checkPathing();
        if (!errors.isEmpty()) {
            DefaultListModel lm = new DefaultListModel();
            for (String s : errors) {
                lm.addElement(s);
            }
            JList lst = new JList(lm);
            lst.setVisibleRowCount(5);
            JScrollPane scr = new JScrollPane(lst);
            int ret;
            if (mustAbort) {
                JOptionPane.showMessageDialog(this, new Object[] { "The following errors are present in this map.", "Errors in component IDs prevent the map being saved.", scr }, "Invalid map!", JOptionPane.WARNING_MESSAGE);
                ret = JOptionPane.CANCEL_OPTION;
            } else {
                ret = JOptionPane.showOptionDialog(this, new Object[] { "The following errors are present in this map:", scr }, "Invalid map!", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, new String[] { "Save Anyway", "Cancel" }, null);
            }
            if (ret != JOptionPane.OK_OPTION) {
                return false;
            }
        }
        try {
            if (!f.getName().endsWith(".xmap") && !f.getName().endsWith(".zmap")) {
                f = new File(f.getParentFile(), f.getName() + ft.ext[0]);
            }
            if (f.exists()) {
                if (JOptionPane.showConfirmDialog(this, "This file already exists. Overwrite?", "Overwrite?", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) != JOptionPane.YES_OPTION) {
                    return false;
                }
            }
            f.createNewFile();
            FileOutputStream fos = new FileOutputStream(f);
            if (ft == FileType.ZMAP) {
                GZIPOutputStream zos = new GZIPOutputStream(fos);
                outputXMLData(zos);
                zos.close();
            } else {
                outputXMLData(fos);
            }
            fos.close();
            return true;
        } catch (IOException ex) {
            Logger.getLogger(MapDesigner.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(MapDesigner.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(MapDesigner.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
