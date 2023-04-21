    private void updateCustomer(String Customer, Integer Nomor, Date Tanggal) {
        try {
            transaksiEntity.getTransaction().begin();
            transaksiQuery = transaksiEntity.createNativeQuery("UPDATE transaksi SET customer = #customer WHERE nomor = #nomor AND tanggal = #tanggal").setParameter("customer", Customer).setParameter("nomor", Nomor).setParameter("tanggal", dateSQL.format(Tanggal));
            transaksiQuery.executeUpdate();
            transaksiEntity.getTransaction().commit();
        } catch (Exception ex) {
            transaksiEntity.getTransaction().rollback();
            ex.printStackTrace();
            Logger.getLogger(GeneralConfig.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
