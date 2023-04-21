    public void updateAllDiscount(int Nomor, Date Tanggal, Double Discount) {
        try {
            Discount = Discount / 100;
            transaksiEntity.getTransaction().begin();
            transaksiQuery = transaksiEntity.createNativeQuery("UPDATE detiltransaksi dt SET dt.diskon = (dt.harga * #diskon), dt.pajak = (dt.harga - (dt.harga * #diskon)) * #pajak WHERE dt.nomor = #nomor AND dt.tanggal = #tanggal").setParameter("diskon", Discount).setParameter("nomor", Nomor).setParameter("tanggal", Tanggal).setParameter("pajak", cfg.getVatValue() / 100);
            transaksiQuery.executeUpdate();
            transaksiQuery = transaksiEntity.createNativeQuery("UPDATE detiltransaksi dt SET dt.pajak = 0 WHERE dt.nomor = #nomor AND dt.tanggal = #tanggal AND dt.menu IN (SELECT m.menu FROM menu m WHERE m.pajak = 0)").setParameter("nomor", Nomor).setParameter("tanggal", Tanggal);
            transaksiQuery.executeUpdate();
            transaksiEntity.getTransaction().commit();
        } catch (Exception ex) {
            transaksiEntity.getTransaction().rollback();
            ex.printStackTrace();
            Logger.getLogger(GeneralConfig.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
