    public void updateJumlah(int Nomor, Date Tanggal, int Menu, Double Jumlah) {
        try {
            transaksiEntity.getTransaction().begin();
            transaksiQuery = transaksiEntity.createNativeQuery("UPDATE detiltransaksi dt SET dt.jumlah = #jumlah WHERE dt.nomor = #nomor AND dt.tanggal = #tanggal AND dt.menu = #menu").setParameter("menu", Menu).setParameter("jumlah", Jumlah).setParameter("nomor", Nomor).setParameter("tanggal", dateSQL.format(Tanggal));
            transaksiQuery.executeUpdate();
            transaksiEntity.getTransaction().commit();
        } catch (Exception ex) {
            transaksiEntity.getTransaction().rollback();
            ex.printStackTrace();
            Logger.getLogger(GeneralConfig.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
