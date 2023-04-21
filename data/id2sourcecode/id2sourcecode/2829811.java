    private void execTambahPembayaran(String Jenis, Double Jumlah) throws NumberFormatException {
        try {
            int Kuitansi;
            Integer Nomor = Integer.valueOf(invoiceField.getText());
            Date Tanggal = dateDisplay.parse(tanggalField.getText());
            if (persenCheckBox.isSelected()) {
                Jumlah = cfg.getTotalTransaksi(Nomor, Tanggal) * Jumlah / 100;
            }
            if (cfg.isNegativePay(Jenis)) {
                Jumlah = -Jumlah;
            }
            Kuitansi = cfg.getNomorKuitansi();
            pembayaranEntity.getTransaction().begin();
            pembayaranQuery = pembayaranEntity.createNativeQuery("INSERT INTO pembayaran VALUES (#nobukti, #nomor, #tanggal, #jenis, #jumlah, #namauser, #dibayar, #shift)").setParameter("nobukti", Kuitansi).setParameter("nomor", Nomor).setParameter("tanggal", dateSQL.format(Tanggal)).setParameter("jenis", Jenis).setParameter("jumlah", Jumlah).setParameter("namauser", cfg.getLoggedUser()).setParameter("dibayar", dateSQL.format(new Date())).setParameter("shift", cfg.getCurrentShift());
            pembayaranQuery.executeUpdate();
            cfg.incNomorKuitansi();
            pembayaranEntity.getTransaction().commit();
            resetInputPembayaran();
            refreshTabelPembayaran(Nomor, Tanggal);
        } catch (ParseException ex) {
            pembayaranEntity.getTransaction().rollback();
            ex.printStackTrace();
            Logger.getLogger(PembayaranView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
