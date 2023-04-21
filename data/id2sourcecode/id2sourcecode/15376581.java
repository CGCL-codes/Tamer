    public Boolean addRestrictedPolicy(Integer Module, String Group) {
        Boolean result = false;
        try {
            configEntity.getTransaction().begin();
            configQuery = configEntity.createNativeQuery("INSERT INTO skema VALUES (#module, #group, false)").setParameter("module", Module).setParameter("group", Group);
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
