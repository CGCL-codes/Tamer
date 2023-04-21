    public static boolean xoaHoChieu(String soHC) {
        boolean result = false;
        String sqlHC = "delete from hochieu where sohc = '" + soHC + "'";
        String sqlViTriLuuTru = "delete from vitriluutru where sohc = '" + soHC + "'";
        ConnectDB cont = new ConnectDB();
        cont.connectDB();
        try {
            if (checkSoHCVaCMND(soHC) == false) {
                int i = cont.getStatement().executeUpdate(sqlHC);
                int j = cont.getStatement().executeUpdate(sqlViTriLuuTru);
                if (i > 0 && j > 0) result = true; else {
                    cont.getConnect().rollback();
                    result = false;
                }
            } else {
                String cmnd = getCMND(soHC);
                String sqlCaNhan = "delete from canhan where cmnd = '" + cmnd + "'";
                int i = cont.getStatement().executeUpdate(sqlHC);
                int j = cont.getStatement().executeUpdate(sqlViTriLuuTru);
                int k = cont.getStatement().executeUpdate(sqlCaNhan);
                if (i > 0 && j > 0 && k > 0) result = true; else {
                    cont.getConnect().rollback();
                    result = false;
                }
            }
        } catch (Exception ex) {
            result = false;
        }
        return result;
    }
