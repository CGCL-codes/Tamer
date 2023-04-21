    @Action
    public void ubahHargaMenu() {
        try {
            Integer Menu;
            String inputDialog;
            Double hargaLama = null;
            Double hargaBaru = null;
            hargaLama = Double.valueOf(String.valueOf(menuTable.getValueAt(menuTable.getSelectedRow(), 3)));
            inputDialog = JOptionPane.showInputDialog(menuTable, "Ganti Harga Menu", floatDisplay.format(hargaLama));
            if (!(inputDialog == null) && (!inputDialog.isEmpty())) {
                hargaBaru = Double.valueOf(inputDialog);
            }
            if (!hargaLama.equals(hargaBaru) && !(hargaBaru == null)) {
                Menu = Integer.valueOf(String.valueOf(menuTable.getValueAt(menuTable.getSelectedRow(), 0)));
                menuEntity.getTransaction().begin();
                menuQuery = menuEntity.createNativeQuery("UPDATE menu m SET m.harga = #harga WHERE m.menu = #menu").setParameter("menu", displayMenu.format(Menu)).setParameter("harga", hargaBaru);
                menuQuery.executeUpdate();
                menuEntity.getTransaction().commit();
            }
        } catch (Exception ex) {
            menuEntity.getTransaction().rollback();
            ex.printStackTrace();
            Logger.getLogger(GeneralConfig.class.getName()).log(Level.SEVERE, null, ex);
        }
        refreshTableMenu();
    }
