    private Boolean hapusItem(Integer Nomor, Date Tanggal, Integer Menu) {
        Boolean r = false;
        try {
            transaksiEntity.getTransaction().begin();
            transaksiQuery = transaksiEntity.createNativeQuery("DELETE FROM detiltransaksi WHERE nomor = #nomor AND tanggal = #tanggal AND menu = #menu").setParameter("nomor", invoiceDisplay.format(Nomor)).setParameter("tanggal", dateSQL.format(Tanggal)).setParameter("menu", invoiceDisplay.format(Menu));
            transaksiQuery.executeUpdate();
            transaksiEntity.getTransaction().commit();
        } catch (Exception ex) {
            transaksiEntity.getTransaction().rollback();
            ex.printStackTrace();
            Logger.getLogger(GeneralConfig.class.getName()).log(Level.SEVERE, null, ex);
        }
        return r;
    }
