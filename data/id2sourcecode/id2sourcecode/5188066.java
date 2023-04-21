    private void execTambahPembayaran(String Kelompok, String Keterangan, Double Jumlah) {
        Date Tanggal = null;
        Integer Transaksi = cfg.getNomorKas();
        try {
            Tanggal = dateDisplay.parse(tanggalField.getText());
            kasHarianEntity.getTransaction().begin();
            kasHarianQuery = kasHarianEntity.createNativeQuery("INSERT INTO kas VALUES (#transaksi, #tanggal, #kelompok, #keterangan, #jumlah, #namauser, #shift)").setParameter("transaksi", Transaksi).setParameter("tanggal", dateSQL.format(Tanggal)).setParameter("kelompok", Kelompok).setParameter("keterangan", Keterangan).setParameter("jumlah", Jumlah).setParameter("namauser", cfg.getLoggedUser()).setParameter("shift", cfg.getCurrentShift());
            kasHarianQuery.executeUpdate();
            cfg.incNomorKas();
            kasHarianEntity.getTransaction().commit();
            resetInputKas();
            refreshTableKas(Tanggal);
        } catch (ParseException ex) {
            kasHarianEntity.getTransaction().rollback();
            ex.printStackTrace();
            Logger.getLogger(PembayaranView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
