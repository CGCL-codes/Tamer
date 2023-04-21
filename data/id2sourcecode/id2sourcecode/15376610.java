    public Boolean setUserPassword(String NamaUser, String Password) {
        Boolean result = false;
        try {
            configEntity.getTransaction().begin();
            configQuery = configEntity.createNativeQuery("UPDATE operator SET password = MD5(#password) WHERE namauser = #namauser").setParameter("password", Password).setParameter("namauser", NamaUser);
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
