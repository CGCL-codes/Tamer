    @Action
    public void ubahNamaMenu() {
        try {
            Integer Menu;
            String namaLama = null;
            String namaBaru = null;
            namaLama = String.valueOf(menuTable.getValueAt(menuTable.getSelectedRow(), 2));
            namaBaru = JOptionPane.showInputDialog(menuTable, "Ganti Nama Menu", namaLama);
            if (!namaLama.equals(namaBaru) && !(namaBaru == null)) {
                Menu = Integer.valueOf(String.valueOf(menuTable.getValueAt(menuTable.getSelectedRow(), 0)));
                menuEntity.getTransaction().begin();
                menuQuery = menuEntity.createNativeQuery("UPDATE menu m SET m.nama = #nama WHERE m.menu = #menu").setParameter("menu", displayMenu.format(Menu)).setParameter("nama", namaBaru);
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
