    public Boolean addUserGroup(String Group) {
        Boolean result = false;
        try {
            configEntity.getTransaction().begin();
            configQuery = configEntity.createNativeQuery("INSERT INTO hakakses VALUES (#akses, false)").setParameter("akses", Group);
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
