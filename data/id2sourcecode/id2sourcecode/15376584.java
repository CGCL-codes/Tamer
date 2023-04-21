    public Boolean deleteJenisBayar(String Jenis) {
        Boolean result = false;
        try {
            configEntity.getTransaction().begin();
            configQuery = configEntity.createNativeQuery("DELETE FROM jenisbayar WHERE jenis = #jenis").setParameter("jenis", Jenis);
            configQuery.executeUpdate();
            configEntity.getTransaction().commit();
            result = true;
        } catch (Exception ex) {
            configEntity.getTransaction().rollback();
            ex.printStackTrace();
            Logger.getLogger(GeneralConfig.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
