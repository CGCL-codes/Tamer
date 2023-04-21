    public t_town_base_Form getByID(String ID) throws HibernateException {
        t_town_base_Form vo = null;
        logger.debug("\nt_town_base_QueryMap getByID:" + ID);
        this.session.clear();
        try {
            vo = new t_town_base_Form();
            t_town_base po = (t_town_base) this.session.load(t_town_base.class, new Integer(ID));
            try {
                vo.setId(String.valueOf(po.getId()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            vo.setCode(po.getCode());
            vo.setName(po.getName());
            vo.setCounty(po.getCounty());
            try {
                vo.setInsert_day(com.creawor.km.util.DateUtil.getStr(po.getInsert_day(), "yyyy-MM-dd"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                vo.setPopulation(String.valueOf(po.getPopulation()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            vo.setSec(po.getSec());
            vo.setAlcalde(po.getAlcalde());
            try {
                vo.setX(String.valueOf(po.getX()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                vo.setXzc_num(String.valueOf(po.getXzc_num()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                vo.setYryc_num(String.valueOf(po.getYryc_num()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                vo.setShort_net_num(String.valueOf(po.getShort_net_num()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                vo.setChannel_num(String.valueOf(po.getChannel_num()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                vo.setManager_num(String.valueOf(po.getManager_num()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                vo.setD_manager_num(String.valueOf(po.getD_manager_num()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                vo.setL_manager_num(String.valueOf(po.getL_manager_num()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                vo.setY(String.valueOf(po.getY()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (HibernateException e) {
            logger.debug("\nERROR in getByID @t_town_base:" + e);
        }
        return vo;
    }
