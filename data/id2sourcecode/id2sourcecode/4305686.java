    public void update(t_channel_Form vo) throws HibernateException {
        Session session = this.beginTransaction();
        String id = vo.getId();
        t_channel po = (t_channel) session.load(t_channel.class, new Integer(id));
        try {
            po.setId(java.lang.Integer.parseInt(vo.getId()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            po.setService_hall_code(vo.getService_hall_code());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            po.setService_hall_name(vo.getService_hall_name());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            po.setChannel_type(vo.getChannel_type());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            po.setStar_level(vo.getStar_level());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            po.setAddress(vo.getAddress());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            po.setCompany(vo.getCompany());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            po.setContact_man(vo.getContact_man());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            po.setContact_tel(vo.getContact_tel());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            po.setContact_mobile(vo.getContact_mobile());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            po.setRent(java.lang.Double.parseDouble(vo.getRent()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            po.setTown(vo.getTown());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            po.setTowncode(vo.getTowncode());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            po.setCounty(vo.getCounty());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            po.setMain_type(vo.getMain_type());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            po.setIscomplete(vo.getIscomplete());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            po.setCounty_code(vo.getCounty_code());
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
        try {
            po.setZoom(java.lang.Integer.parseInt(vo.getZoom()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            po.setJindu(vo.getJindu());
        } catch (Exception e) {
            e.printStackTrace();
        }
        po.setVillage_xz(vo.getVillage_xz());
        po.setVillage_zr(vo.getVillage_zr());
        po.setParent(vo.getParent());
        try {
            session.update(po);
            this.endTransaction(true);
        } catch (Exception e) {
            this.endTransaction(false);
            e.printStackTrace();
            throw new HibernateException(e);
        }
    }
