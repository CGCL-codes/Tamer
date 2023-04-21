    public String iamOnLineToAll(String clijson) {
        log.info("Start iam " + clijson);
        ABeanFactory factory = AutoBeanFactorySource.create(ABeanFactory.class);
        AutoBeanHandlerImpl autoBeanHandlerImpl = new AutoBeanHandlerImpl();
        String iam = autoBeanHandlerImpl.deserializeCliCookieFromJson(factory, clijson).getId();
        ChannelService channelService = ChannelServiceFactory.getChannelService();
        statement = "SELECT id,nickname from online where status='connected'";
        c = null;
        try {
            DriverManager.registerDriver(new AppEngineDriver());
            c = (Connection) DriverManager.getConnection("jdbc:google:rdbms://sinelga.com:sinelgamysql:sinelga/keskustelu");
            Statement st = c.createStatement();
            ResultSet rs = st.executeQuery(statement);
            List<OnLineAutoBean> listOnLineAutoBean = new ArrayList<OnLineAutoBean>();
            while (rs.next()) {
                log.info("Send to " + rs.getString(1) + " name " + rs.getString(2));
                OnLineAutoBean onLineAutoBean = autoBeanHandlerImpl.makeOnLineAutoBean(factory);
                onLineAutoBean.setId(rs.getString(1));
                onLineAutoBean.setNickname(rs.getString(2));
                listOnLineAutoBean.add(onLineAutoBean);
                if (!rs.getString(1).equals(iam)) {
                    ChannelMessage channelMessage = new ChannelMessage(rs.getString(1), clijson);
                    channelService.sendMessage(channelMessage);
                }
            }
            AllOnLineAutoBean allOnLineAutoBean = autoBeanHandlerImpl.makeAllOnLineAutoBean(factory);
            allOnLineAutoBean.setAllOnLineAutoBean(listOnLineAutoBean);
            json = autoBeanHandlerImpl.serializeToJsonAllOnLineAutoBean(allOnLineAutoBean);
        } catch (SQLException e) {
            log.severe(e.getMessage());
        }
        return json;
    }
