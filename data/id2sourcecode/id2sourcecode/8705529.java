    @Action
    public void ubahKelompokMenu() {
        try {
            Integer Menu;
            String kelompokLama = null;
            String kelompokBaru = null;
            kelompokLama = String.valueOf(menuTable.getValueAt(menuTable.getSelectedRow(), 1));
            kelompokBaru = JOptionPane.showInputDialog(menuTable, "Ganti Kelompok Menu", kelompokLama);
            if (!kelompokLama.equals(kelompokBaru) && !(kelompokBaru == null)) {
                Menu = Integer.valueOf(String.valueOf(menuTable.getValueAt(menuTable.getSelectedRow(), 0)));
                menuEntity.getTransaction().begin();
                menuQuery = menuEntity.createNativeQuery("UPDATE menu m SET m.kelompok = #kelompok WHERE m.menu = #menu").setParameter("menu", displayMenu.format(Menu)).setParameter("kelompok", kelompokBaru);
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
