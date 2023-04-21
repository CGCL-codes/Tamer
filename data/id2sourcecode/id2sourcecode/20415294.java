    private void bt_SaveActionPerformed(java.awt.event.ActionEvent evt) {
        JFileChooser jfc = new JFileChooser();
        jfc.setDialogType(JFileChooser.SAVE_DIALOG);
        jfc.setMultiSelectionEnabled(false);
        if (jfc.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;
        StyledDocument doc = (StyledDocument) epn_Summary.getDocument();
        RTFEditorKit kit = new RTFEditorKit();
        BufferedOutputStream bos = null;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(jfc.getSelectedFile().getAbsoluteFile()));
            kit.write(bos, doc, doc.getStartPosition().getOffset(), doc.getLength());
            GUI.launchInfo("Network save correctly ( " + jfc.getSelectedFile().getAbsoluteFile() + " )");
        } catch (FileNotFoundException ex) {
            pn_Info.Load((JPanel) this.getParent(), this, "Error", "Path or File wasn't found to write<p>" + ex.getLocalizedMessage(), pn_Info.ICON_ERROR);
        } catch (IOException ex) {
            pn_Info.Load((JPanel) this.getParent(), this, "Error", "File is read-only or write-protected. Check it and try again<p>" + ex.getLocalizedMessage(), pn_Info.ICON_ERROR);
        } catch (BadLocationException ex) {
            pn_Info.Load((JPanel) this.getParent(), this, "Error", "Data couldn't be saved<p>" + ex.getLocalizedMessage(), pn_Info.ICON_ERROR);
        } finally {
            try {
                if (bos != null) bos.close();
            } catch (IOException ex) {
                pn_Info.Load((JPanel) this.getParent(), this, "Error", "File couldn't be closed!<br>Maybe file won't be accessible while kuasar is opened<p>" + ex.getLocalizedMessage(), pn_Info.ICON_ERROR);
            }
        }
    }
