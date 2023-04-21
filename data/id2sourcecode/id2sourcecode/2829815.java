    public Boolean execHapusPembayaran(Integer NoBukti, Date Dibayar) {
        Double Nilai;
        String Jenis;
        Boolean r = false;
        Jenis = String.valueOf(pembayaranTable.getValueAt(pembayaranTable.getSelectedRow(), 2));
        Nilai = Double.valueOf(String.valueOf(pembayaranTable.getValueAt(pembayaranTable.getSelectedRow(), 3)));
        if (JOptionPane.showConfirmDialog(this, "Hapus pembayaran " + Jenis + " sejumlah " + floatDisplay.format(Nilai) + "?", "Konfirmasi", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            try {
                pembayaranEntity.getTransaction().begin();
                pembayaranQuery = pembayaranEntity.createNativeQuery("DELETE FROM pembayaran WHERE nobukti = #nobukti AND dibayar = #dibayar").setParameter("nobukti", NoBukti).setParameter("dibayar", Dibayar);
                pembayaranQuery.executeUpdate();
                pembayaranEntity.getTransaction().commit();
                r = true;
            } catch (Exception ex) {
                pembayaranEntity.getTransaction().rollback();
                ex.printStackTrace();
                Logger.getLogger(GeneralConfig.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return r;
    }
