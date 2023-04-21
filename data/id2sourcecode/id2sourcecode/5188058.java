    private Boolean execHapusKasHarian(Integer Transaksi, Date Tanggal) {
        Boolean result = false;
        String Uraian;
        Double Nilai;
        Uraian = String.valueOf(kasHarianTable.getValueAt(kasHarianTable.getSelectedRow(), 2));
        Nilai = Double.valueOf(String.valueOf(kasHarianTable.getValueAt(kasHarianTable.getSelectedRow(), 3)));
        if (JOptionPane.showConfirmDialog(this, "Hapus " + Uraian + " sejumlah " + floatDisplay.format(Nilai) + "?", "Konfirmasi", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            try {
                kasHarianEntity.getTransaction().begin();
                kasHarianQuery = kasHarianEntity.createNativeQuery("DELETE FROM kas WHERE transaksi = #transaksi AND tanggal = #tanggal").setParameter("transaksi", Transaksi).setParameter("tanggal", dateSQL.format(Tanggal));
                kasHarianQuery.executeUpdate();
                kasHarianEntity.getTransaction().commit();
                result = true;
            } catch (Exception ex) {
                kasHarianEntity.getTransaction().rollback();
                ex.printStackTrace();
                Logger.getLogger(GeneralConfig.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }
