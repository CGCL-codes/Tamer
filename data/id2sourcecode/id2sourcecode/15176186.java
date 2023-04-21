    @Override
    public String iamOnLineToAll(String clijson) {
        ABeanFactory factory = AutoBeanFactorySource.create(ABeanFactory.class);
        AutoBeanHandlerImpl autoBeanHandlerImpl = new AutoBeanHandlerImpl();
        List<OnLineAutoBean> listOnLineAutoBean = new ArrayList<OnLineAutoBean>();
        if (!clijson.equals("refresh")) {
            listCliCookieImpl = findAll();
            CliCookie cliCookie = autoBeanHandlerImpl.deserializeCliCookieFromJson(factory, clijson);
            iam = cliCookie.getId();
            for (CliCookieImpl cliCookieImpl : listCliCookieImpl) {
                ChannelService channelService = ChannelServiceFactory.getChannelService();
                if (!cliCookieImpl.getId().equals(iam)) {
                    OnLineAutoBean onLineAutoBean = autoBeanHandlerImpl.makeOnLineAutoBean(factory);
                    onLineAutoBean.setId(cliCookieImpl.getId());
                    onLineAutoBean.setNickname(cliCookieImpl.getNickname());
                    onLineAutoBean.setAvatar(cliCookieImpl.getAvatar());
                    listOnLineAutoBean.add(onLineAutoBean);
                    MessageAutoBean messageAutoBean = autoBeanHandlerImpl.makeMessageAutoBean(factory);
                    messageAutoBean.setAbout("manconnected");
                    messageAutoBean.setContext(clijson);
                    String jsonstr = autoBeanHandlerImpl.serializeToJsonMessageAutoBean(messageAutoBean);
                    ChannelMessage newCliConnectedMsg = new ChannelMessage(cliCookieImpl.getId(), jsonstr);
                    channelService.sendMessage(newCliConnectedMsg);
                }
            }
        } else {
            listCliCookieImpl = findAll();
            for (CliCookieImpl cliCookieImpl : listCliCookieImpl) {
                OnLineAutoBean onLineAutoBean = autoBeanHandlerImpl.makeOnLineAutoBean(factory);
                onLineAutoBean.setId(cliCookieImpl.getId());
                onLineAutoBean.setNickname(cliCookieImpl.getNickname());
                onLineAutoBean.setAvatar(cliCookieImpl.getAvatar());
                listOnLineAutoBean.add(onLineAutoBean);
            }
            cronservletcount = cronservletcount + 1;
            if (cronservletcount > 4) {
                cronservletcount = 0;
                Queue queue = QueueFactory.getDefaultQueue();
                queue.add(TaskOptions.Builder.withUrl("/cronservlet"));
            }
        }
        AllOnLineAutoBean allOnLineAutoBean = autoBeanHandlerImpl.makeAllOnLineAutoBean(factory);
        allOnLineAutoBean.setAllOnLineAutoBean(listOnLineAutoBean);
        json = autoBeanHandlerImpl.serializeToJsonAllOnLineAutoBean(allOnLineAutoBean);
        return json;
    }
