    public void newDetilTransaksi(int Nomor, Date Tanggal, int Menu, Double Jumlah, Double Harga, Double Diskon, Double Pajak, String NamaUser, Double Berat) {
        try {
            transaksiEntity.getTransaction().begin();
            transaksiQuery = transaksiEntity.createNativeQuery("SELECT dt.* FROM detiltransaksi dt WHERE dt.nomor = #nomor AND dt.tanggal = #tanggal AND dt.menu = #menu").setParameter("nomor", Nomor).setParameter("tanggal", Tanggal).setParameter("menu", Menu);
            if (transaksiQuery.getResultList().isEmpty()) {
                transaksiQuery = transaksiEntity.createNativeQuery("INSERT INTO detiltransaksi VALUES (#nomor, #tanggal, #menu, #jumlah, #harga, #diskon, #pajak, #namauser, #berat)").setParameter("nomor", Nomor).setParameter("tanggal", Tanggal).setParameter("menu", Menu).setParameter("jumlah", Jumlah).setParameter("harga", Harga).setParameter("diskon", Diskon).setParameter("pajak", Pajak).setParameter("namauser", NamaUser).setParameter("berat", Berat);
                transaksiQuery.executeUpdate();
            } else {
                transaksiQuery = transaksiEntity.createNativeQuery("UPDATE detiltransaksi dt SET dt.jumlah = (dt.jumlah + #jumlah), dt.berat = (dt.berat + #berat) WHERE dt.nomor = #nomor AND dt.tanggal = #tanggal AND dt.menu = #menu").setParameter("nomor", invoiceDisplay.format(Nomor)).setParameter("tanggal", dateSQL.format(Tanggal)).setParameter("menu", invoiceDisplay.format(Menu)).setParameter("jumlah", Jumlah).setParameter("berat", Berat);
                transaksiQuery.executeUpdate();
            }
            transaksiEntity.getTransaction().commit();
        } catch (Exception ex) {
            transaksiEntity.getTransaction().rollback();
            ex.printStackTrace();
            Logger.getLogger(GeneralConfig.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
