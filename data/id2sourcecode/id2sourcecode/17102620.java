    @Override
    public void doService() {
        List<String> list = dao.getList();
        ng = new NameGenerator("languages.txt", "semantics.txt");
        String[] namegn = ng.getRandomName("Finnish", "Female", 1);
        ABeanFactory factory = AutoBeanFactorySource.create(ABeanFactory.class);
        AutoBeanHandlerImpl autoBeanHandlerImpl = new AutoBeanHandlerImpl();
        ChannelService channelService = ChannelServiceFactory.getChannelService();
        for (String data : list) {
            log.info(data);
            MessageAutoBean messageAutoBean = autoBeanHandlerImpl.makeMessageAutoBean(factory);
            messageAutoBean.setAbout("girlconnected");
            messageAutoBean.setContext("Connected: " + namegn[0]);
            String jsonstr = autoBeanHandlerImpl.serializeToJsonMessageAutoBean(messageAutoBean);
            ChannelMessage message = new ChannelMessage(data, jsonstr);
            channelService.sendMessage(message);
        }
    }
