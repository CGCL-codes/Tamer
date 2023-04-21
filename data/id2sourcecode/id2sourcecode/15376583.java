    public Boolean addJenisBayar(String Jenis, Boolean Negasi) {
        Boolean result = false;
        try {
            configEntity.getTransaction().begin();
            configQuery = configEntity.createNativeQuery("INSERT INTO jenisbayar VALUES (#jenis, #negasi)").setParameter("jenis", Jenis).setParameter("negasi", Negasi);
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
