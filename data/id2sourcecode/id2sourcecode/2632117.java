    public static void main(String args[]) {
        if (logger.isDebugEnabled()) {
            logger.debug("main(String[]) - start");
        }
        try {
            ChannelHandle ex = new ChannelHandle();
            String file = "f:/shehuiqudao(cmp).xls";
            ex.setString("2008-08-08");
            ex.readExcel(file);
            ex.insertInfo();
            for (int i = 0; i < ex.m_channelAL.size(); i++) {
                t_channel vo = (t_channel) ex.m_channelAL.get(i);
                System.out.println("VO:::" + vo.getAddress());
                System.out.println("PublishdateVO:::" + vo.getAddress());
            }
            for (int i = 0; i < ex.m_channelSaleAL.size(); i++) {
                t_channel_sale vo = (t_channel_sale) ex.m_channelSaleAL.get(i);
                System.out.println("VO:::" + vo.getChannel_name());
            }
        } catch (Exception e) {
            logger.error("main(String[])", e);
            e.printStackTrace();
        }
        if (logger.isDebugEnabled()) {
            logger.debug("main(String[]) - end");
        }
    }
