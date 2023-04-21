    public void updateBerat(int Nomor, Date Tanggal, int Menu, Double Berat) {
        try {
            transaksiEntity.getTransaction().begin();
            transaksiQuery = transaksiEntity.createNativeQuery("UPDATE detiltransaksi dt SET dt.berat = #berat WHERE dt.nomor = #nomor AND dt.tanggal = #tanggal AND dt.menu = #menu").setParameter("menu", Menu).setParameter("berat", Berat).setParameter("nomor", Nomor).setParameter("tanggal", dateSQL.format(Tanggal));
            transaksiQuery.executeUpdate();
            transaksiEntity.getTransaction().commit();
        } catch (Exception ex) {
            transaksiEntity.getTransaction().rollback();
            ex.printStackTrace();
            Logger.getLogger(GeneralConfig.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
