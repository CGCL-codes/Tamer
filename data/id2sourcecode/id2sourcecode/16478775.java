    public void newTransaksi(int Nomor, Date Tanggal, int Shift, String Customer, String NamaUser) {
        try {
            transaksiEntity.getTransaction().begin();
            transaksiQuery = transaksiEntity.createNativeQuery("INSERT INTO transaksi VALUES (#nomor, #tanggal, #customer, #namauser, #shift, #posted)").setParameter("nomor", Nomor).setParameter("tanggal", dateSQL.format(Tanggal)).setParameter("customer", Customer).setParameter("namauser", NamaUser).setParameter("shift", Shift).setParameter("posted", false);
            transaksiQuery.executeUpdate();
            transaksiEntity.getTransaction().commit();
        } catch (Exception ex) {
            transaksiEntity.getTransaction().rollback();
            ex.printStackTrace();
            Logger.getLogger(GeneralConfig.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
