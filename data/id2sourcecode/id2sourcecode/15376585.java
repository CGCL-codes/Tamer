    public Boolean addOperator(String NamaUser, String Password, String NamaAsli, String Akses) {
        Boolean result = false;
        try {
            configEntity.getTransaction().begin();
            configQuery = configEntity.createNativeQuery("INSERT INTO operator VALUES (#namauser, MD5(#password), #namaasli, #akses)").setParameter("namauser", NamaUser).setParameter("password", Password).setParameter("namaasli", NamaAsli).setParameter("akses", Akses);
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
