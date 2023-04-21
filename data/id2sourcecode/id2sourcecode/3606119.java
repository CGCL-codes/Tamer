    public void add(t_town_base_Form vo) throws HibernateException {
        t_town_base po = new t_town_base();
        try {
            po.setId(java.lang.Integer.parseInt(vo.getId()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            po.setCode(vo.getCode());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            po.setName(vo.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            po.setCounty(vo.getCounty());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            po.setInsert_day(com.creawor.km.util.DateUtil.getDate(vo.getInsert_day(), "yyyy-MM-dd"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            po.setPopulation(java.lang.Integer.parseInt(vo.getPopulation()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            po.setXzc_num((vo.getXzc_num()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            po.setYryc_num((vo.getYryc_num()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            po.setShort_net_num((vo.getShort_net_num()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            po.setChannel_num((vo.getChannel_num()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            po.setManager_num((vo.getManager_num()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            po.setD_manager_num((vo.getD_manager_num()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            po.setL_manager_num((vo.getL_manager_num()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            po.setSec(vo.getSec());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            po.setAlcalde(vo.getAlcalde());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            po.setX(java.lang.Double.parseDouble(vo.getX()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            po.setY(java.lang.Double.parseDouble(vo.getY()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Session session = this.beginTransaction();
        try {
            session.save(po);
            this.endTransaction(true);
        } catch (Exception e) {
            this.endTransaction(false);
            e.printStackTrace();
            throw new HibernateException(e);
        }
    }
