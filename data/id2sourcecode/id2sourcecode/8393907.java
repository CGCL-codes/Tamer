    public t_channel_sale_Form getByID(String ID) throws HibernateException {
        t_channel_sale_Form vo = null;
        System.out.println("\nt_channel_sale_QueryMap getByID:" + ID);
        this.session.clear();
        try {
            vo = new t_channel_sale_Form();
            t_channel_sale po = (t_channel_sale) this.session.load(t_channel_sale.class, new Integer(ID));
            try {
                vo.setId(String.valueOf(po.getId()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            vo.setThe_month(po.getThe_month());
            vo.setChannel_code(po.getChannel_code());
            vo.setChannel_name(po.getChannel_name());
            try {
                vo.setUpdated_day(com.creawor.km.util.DateUtil.getStr(po.getUpdated_day(), "yyyy-MM-dd"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                vo.setRecompense(String.valueOf((po.getRecompense())));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                vo.setCharge_avg(String.valueOf(po.getCharge_avg()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                vo.setCard_sale_avg(String.valueOf(po.getCard_sale_avg()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                vo.setInsert_day((String.valueOf(po.getInsert_day())));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                vo.setOpentype((String.valueOf(po.getOpentype())));
            } catch (Exception e) {
                e.printStackTrace();
            }
            vo.setCompany(po.getCompany());
            try {
                vo.setCard_apply_avg(String.valueOf(po.getCard_apply_avg()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (HibernateException e) {
            System.out.println("\nERROR in getByID @t_channel_sale:" + e);
        }
        return vo;
    }
