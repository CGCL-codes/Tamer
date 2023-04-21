    @Action
    public void tambahMenu() {
        String kelompok = "";
        if (baruCheckBox.isSelected()) {
            kelompok = kelompokBaruField.getText();
        } else {
            kelompok = String.valueOf(kelompokField.getSelectedItem());
        }
        if (!kelompok.isEmpty() && !namaField.getText().isEmpty() && !(Double.valueOf(hargaField.getText()) <= 0)) {
            try {
                menuEntity.getTransaction().begin();
                menuQuery = menuEntity.createNativeQuery("INSERT INTO menu VALUES (null, #kelompok, #nama, #harga, #aktif, #pajak, #satuan)").setParameter("kelompok", kelompok).setParameter("nama", namaField.getText()).setParameter("harga", Double.valueOf(hargaField.getText())).setParameter("aktif", true).setParameter("pajak", !nonPajakField.isSelected()).setParameter("satuan", String.valueOf(satuanField.getText()));
                menuQuery.executeUpdate();
                menuEntity.getTransaction().commit();
                resetInputForm();
                refreshTableMenu();
            } catch (Exception ex) {
                menuEntity.getTransaction().rollback();
                ex.printStackTrace();
                Logger.getLogger(GeneralConfig.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Harap isi seluruh field yang tersedia");
            kelompokField.requestFocus();
        }
    }
