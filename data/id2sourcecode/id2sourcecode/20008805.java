    public void auditRegister(HttpServletRequest request) {
        Register reg = new Register();
        reg.id = StrFun.getInt(request, "rid");
        Register register = findPojoById(reg.id + "", Register.class);
        if (register.audit) throw new ApplicationException("�Ѿ���˹����ٴ����!");
        RegisterInfoDao infoDao = new RegisterInfoDao();
        List<RegisterInfo> list = infoDao.findListByRid("" + reg.id);
        if (list == null || list.size() <= 0) throw new ApplicationException("��������������Ϣ���ݲ�����ˣ�");
        DBConnect ddc = null;
        Connection conn = null;
        try {
            ddc = DBConnect.createDBConnect();
            conn = ddc.getConnection();
            conn.setAutoCommit(false);
            for (RegisterInfo rInfo : list) {
                String sql = "update SS_Product set " + "productAmount=productAmount+" + rInfo.regAmount + ", " + "productStorage=productStorage+" + rInfo.regAmount + ", " + "productPrice=" + rInfo.regPrice + ", " + "productSalePrice=" + rInfo.regSalePrice + " " + "where id=" + rInfo.pid;
                conn.createStatement().executeUpdate(sql);
            }
            String sql = "update SS_Register set audit=1, auditDate='" + GetDate.dateToStr(new Date()) + "' where id=" + reg.id;
            conn.createStatement().executeUpdate(sql);
            conn.commit();
        } catch (Exception e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
            throw new ApplicationException("���ʧ�ܣ�", e);
        } finally {
            if (ddc != null) {
                try {
                    ddc.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
