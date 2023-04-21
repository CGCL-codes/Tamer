    public Boolean deleteRestrictedPolicy(Integer Module, String Group) {
        Boolean result = false;
        try {
            configEntity.getTransaction().begin();
            configQuery = configEntity.createNativeQuery("DELETE FROM skema WHERE modul = #module AND akses = #group").setParameter("module", Module).setParameter("group", Group);
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
