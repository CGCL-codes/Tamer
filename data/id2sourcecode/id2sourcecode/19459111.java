    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws IOException, ServletException {
        Globa globa = new Globa();
        try {
            String sql = "INSERT into  t_sy_logHistory " + "SELECT * FROM t_sy_log WHERE sysdate-dOccurDate>=30 ";
            System.out.println("sql in TransmitLogBean is ==" + sql);
            globa.db.executeUpdate(sql);
            sql = "DELETE t_sy_log WHERE sysdate-dOccurDate>=30";
            globa.db.executeUpdate(sql);
            globa.db.commit();
            globa.closeCon();
            System.out.println("ת����־�ɹ�����");
        } catch (SQLException e) {
            if (globa.db != null) {
                try {
                    globa.db.rollback();
                    globa.db.closeCon();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
            System.out.println("ת����־ʱ�������");
            e.printStackTrace();
        }
    }
