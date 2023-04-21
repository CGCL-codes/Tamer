    public void setConfigValue(String Nama, String Nilai) {
        configQuery = configEntity.createQuery("SELECT k FROM Konfigurasi k WHERE k.nama = :nama").setParameter("nama", Nama);
        if (configQuery.getResultList().isEmpty()) {
            try {
                configEntity.getTransaction().begin();
                configQuery = configEntity.createNativeQuery("INSERT INTO konfigurasi VALUES (#nama, #nilai)").setParameter("nama", Nama).setParameter("nilai", Nilai);
                configQuery.executeUpdate();
                configEntity.getTransaction().commit();
            } catch (Exception ex) {
                configEntity.getTransaction().rollback();
                ex.printStackTrace();
                Logger.getLogger(GeneralConfig.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            try {
                configEntity.getTransaction().begin();
                configQuery = configEntity.createNativeQuery("UPDATE konfigurasi k SET k.nilai = #nilai WHERE k.nama = #nama").setParameter("nama", Nama).setParameter("nilai", Nilai);
                configQuery.executeUpdate();
                configEntity.getTransaction().commit();
            } catch (Exception ex) {
                configEntity.getTransaction().rollback();
                ex.printStackTrace();
                Logger.getLogger(GeneralConfig.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
