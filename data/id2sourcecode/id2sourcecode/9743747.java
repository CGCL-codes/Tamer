    public boolean weeklyHoidayRegistered(String dayName, String flag) {
        boolean check = false;
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            System.out.println("yes herer holidays");
            String hql = "update WeeklyHolidays as w set w.flag='" + flag + "' where w.dayName='" + dayName + "' ";
            Query query = session.createQuery(hql);
            System.out.println("yes herer holidays down");
            WeeklyHolidays weekObj = new WeeklyHolidays();
            weekObj.setFlag(flag);
            int rowcount = query.executeUpdate();
            transaction.commit();
            session.close();
            check = true;
        } catch (HibernateException e) {
            transaction.rollback();
            e.printStackTrace();
        }
        return check;
    }
