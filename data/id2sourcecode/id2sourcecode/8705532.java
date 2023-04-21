    @Action
    public void ubahAktifMenu() {
        try {
            Integer Menu;
            Menu = Integer.valueOf(String.valueOf(menuTable.getValueAt(menuTable.getSelectedRow(), 0)));
            menuEntity.getTransaction().begin();
            menuQuery = menuEntity.createNativeQuery("UPDATE menu m SET m.aktif = not (m.aktif) WHERE m.menu = #menu").setParameter("menu", displayMenu.format(Menu));
            menuQuery.executeUpdate();
            menuEntity.getTransaction().commit();
        } catch (Exception ex) {
            menuEntity.getTransaction().rollback();
            ex.printStackTrace();
            Logger.getLogger(GeneralConfig.class.getName()).log(Level.SEVERE, null, ex);
        }
        refreshTableMenu();
    }
