    public t_village_signal_Form getByID(String ID) throws HibernateException {
        t_village_signal_Form vo = null;
        logger.debug("\nt_village_signal_QueryMap getByID:" + ID);
        this.session.clear();
        try {
            vo = new t_village_signal_Form();
            t_village_signal po = (t_village_signal) this.session.load(t_village_signal.class, new Integer(ID));
            try {
                vo.setId(String.valueOf(po.getId()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            vo.setVillage_code(po.getVillage_code());
            vo.setName(po.getName());
            vo.setSignal(po.getSignal());
            vo.setTown(po.getTown());
            vo.setVillage_xz(po.getVillage_xz());
            vo.setVillage_zr(po.getVillage_zr());
            vo.setCounty(po.getCounty());
            vo.setChannel_code(po.getChannel_code());
            vo.setIs_covered(po.getIs_covered());
            vo.setVillage(po.getVillage());
            vo.setSound_quality(po.getSound_quality());
            try {
                vo.setCover_num(String.valueOf(po.getCover_num()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            vo.setRival_info(po.getRival_info());
            vo.setCovered_name(String.valueOf(po.getCovered_name()));
            try {
                vo.setX(String.valueOf((po.getX())));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                vo.setY(String.valueOf((po.getY())));
            } catch (Exception e) {
                e.printStackTrace();
            }
            vo.setIscreate_net(po.getIscreate_net());
            vo.setIseveryone(po.getIseveryone());
            try {
                vo.setInsert_day(com.creawor.km.util.DateUtil.getStr(po.getInsert_day(), "yyyy-MM-dd"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (HibernateException e) {
            logger.debug("\nERROR in getByID @t_village_signal:" + e);
        }
        return vo;
    }
