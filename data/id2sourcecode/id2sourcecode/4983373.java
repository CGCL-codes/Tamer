    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws IOException, ServletException {
        com.siteeval.common.Globa globa = new com.siteeval.common.Globa();
        globa.initialize(getServletContext());
        try {
            String sql = "INSERT t_sy_logHistory " + "SELECT * FROM t_sy_log WHERE DATEDIFF(day, t_sy_log.dOccurDate, getdate())<=30";
            globa.db.setAutoCommit(false);
            globa.db.executeUpdate(sql);
            sql = "DELETE t_log WHERE DATEDIFF(day, t_sy_log.dOccurDate, getdate()) <= 30";
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
