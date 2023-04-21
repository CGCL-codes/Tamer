    private String insertData(ArrayList al, String type) throws UploadException {
        if (logger.isDebugEnabled()) {
            logger.debug("insertData(ArrayList, String) - start");
        }
        String rsStr = "";
        Session sels = null;
        try {
            SessionFactory sf = (new Configuration()).configure().buildSessionFactory();
            SessionFactory upsf = (new Configuration()).configure().buildSessionFactory();
            Session session = null;
            sels = sf.openSession();
            java.sql.Connection con = sels.connection();
            if (type == "channel") {
                t_channel cvo = null;
                for (int i = 0; i < al.size(); i++) {
                    session = upsf.openSession();
                    Transaction tx = session.beginTransaction();
                    try {
                        cvo = (t_channel) al.get(i);
                        String code = cvo.getService_hall_code();
                        Date day = cvo.getInsert_day();
                        String dateStr = DateUtil.getStr(day, null);
                        String company = cvo.getCompany();
                        dateStr = dateStr.substring(0, 7);
                        String sqlStr = "select * from t_channel where service_hall_code= '" + code + "' and company='" + company + "'  and left(convert(varchar(10),insert_day,120),7)='" + dateStr + "'";
                        ResultSet rs = con.createStatement().executeQuery(sqlStr);
                        if (rs.next()) {
                            cvo.setId(rs.getInt("id"));
                            session.update(cvo);
                        } else {
                            session.save(cvo);
                        }
                        rs.close();
                        tx.commit();
                    } catch (Exception e) {
                        logger.error("insertData(ArrayList, String)", e);
                        rsStr += "��������Ϊ" + cvo.getService_hall_code() + "�������Ϊ" + cvo.getService_hall_name() + "����ʧ�ܣ�<br>";
                        continue;
                    }
                }
            }
            if (type == "channelsale") {
                t_channel_sale csvo = null;
                for (int i = 0; i < al.size(); i++) {
                    session = upsf.openSession();
                    Transaction tx = session.beginTransaction();
                    try {
                        csvo = (t_channel_sale) al.get(i);
                        String code = csvo.getChannel_code();
                        java.util.Date date = csvo.getInsert_day();
                        String dateStr = DateUtil.getStr(date, null);
                        if (null != dateStr) {
                            dateStr = dateStr.substring(0, 7);
                        }
                        String company = csvo.getCompany();
                        String sqlStr = "select * from t_channel_sale where channel_code= '" + code + "' and left(convert(varchar(10),insert_day,120),7)='" + dateStr + "' and company='" + company + "'";
                        ResultSet rs = con.createStatement().executeQuery(sqlStr);
                        if (rs.next()) {
                            csvo.setId(rs.getInt("id"));
                            session.update(csvo);
                        } else {
                            session.save(csvo);
                        }
                        rs.close();
                    } catch (Exception e) {
                        logger.error("insertData(ArrayList, String)", e);
                        e.printStackTrace();
                    }
                    tx.commit();
                }
            }
            session.flush();
            session.close();
            sels.close();
        } catch (HibernateException e) {
            logger.error("insertData(ArrayList, String)", e);
            rsStr = e.getMessage();
        }
        if (logger.isDebugEnabled()) {
            logger.debug("insertData(ArrayList, String) - end");
        }
        return rsStr;
    }
