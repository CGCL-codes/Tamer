    private Boolean execPostingInvoice(Integer Nomor, Date Tanggal) {
        Boolean r = false;
        try {
            transaksiEntity.getTransaction().begin();
            transaksiQuery = transaksiEntity.createNativeQuery("UPDATE transaksi SET posted = true WHERE nomor = #nomor AND tanggal = #tanggal").setParameter("nomor", invoiceDisplay.format(Nomor)).setParameter("tanggal", dateSQL.format(Tanggal));
            transaksiQuery.executeUpdate();
            transaksiEntity.getTransaction().commit();
            openTransaksiForm(0, Tanggal);
            lockTransaksiForm();
        } catch (Exception ex) {
            transaksiEntity.getTransaction().rollback();
            ex.printStackTrace();
            Logger.getLogger(GeneralConfig.class.getName()).log(Level.SEVERE, null, ex);
        }
        return r;
    }
