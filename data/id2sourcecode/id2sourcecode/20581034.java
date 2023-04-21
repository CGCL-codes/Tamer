    public t_channel_Form getByID(String ID) throws HibernateException {
        t_channel_Form vo = null;
        logger.debug("\nt_channel_QueryMap getByID:" + ID);
        this.session.clear();
        try {
            vo = new t_channel_Form();
            t_channel po = (t_channel) this.session.load(t_channel.class, new Integer(ID));
            try {
                vo.setId(String.valueOf(po.getId()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            vo.setService_hall_code(po.getService_hall_code());
            vo.setService_hall_name(po.getService_hall_name());
            vo.setChannel_type(po.getChannel_type());
            vo.setStar_level(po.getStar_level());
            vo.setAddress(po.getAddress());
            vo.setCompany(po.getCompany());
            vo.setContact_man(po.getContact_man());
            vo.setContact_tel(po.getContact_tel());
            vo.setContact_mobile(po.getContact_mobile());
            vo.setVillage_xz(po.getVillage_xz());
            vo.setVillage_zr(po.getVillage_zr());
            vo.setParent(po.getParent());
            vo.setJindu(po.getJindu());
            vo.setMain_type(po.getMain_type());
            vo.setIscomplete(po.getIscomplete());
            try {
                vo.setRent(String.valueOf((po.getRent())));
            } catch (Exception e) {
                e.printStackTrace();
            }
            vo.setTown(po.getTown());
            vo.setTowncode(po.getTowncode());
            vo.setCounty(po.getCounty());
            vo.setCounty_code(po.getCounty_code());
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
            try {
                vo.setZoom(String.valueOf(po.getZoom()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (HibernateException e) {
            logger.debug("\nERROR in getByID @t_channel:" + e);
        }
        return vo;
    }
