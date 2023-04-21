    @Action
    public void ubahSatuanMenu() {
        try {
            Integer Menu;
            String satuanLama = null;
            String satuanBaru = null;
            satuanLama = String.valueOf(menuTable.getValueAt(menuTable.getSelectedRow(), 4));
            satuanBaru = JOptionPane.showInputDialog(menuTable, "Ganti Satuan Menu", satuanLama);
            if (!satuanLama.equals(satuanBaru) && !(satuanBaru == null)) {
                Menu = Integer.valueOf(String.valueOf(menuTable.getValueAt(menuTable.getSelectedRow(), 0)));
                menuEntity.getTransaction().begin();
                menuQuery = menuEntity.createNativeQuery("UPDATE menu m SET m.satuan = #satuan WHERE m.menu = #menu").setParameter("menu", displayMenu.format(Menu)).setParameter("satuan", satuanBaru);
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
