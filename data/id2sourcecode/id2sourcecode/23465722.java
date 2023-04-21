    public JmsTemplateHolder getJmsTemplate(final IBizDriver bizDriver, final ITransactionContext transactionContext) throws JMSException, InstantiationException, IllegalAccessException, ClassNotFoundException, XAwareException {
        final IChannelKey key = bizDriver.getChannelSpecificationKey();
        JmsTemplateHolder jmsTemplateHolder = (JmsTemplateHolder) transactionContext.getTransactionalChannel(key);
        if (jmsTemplateHolder != null) {
            return jmsTemplateHolder;
        }
        JmsConnectionFactoryHolder cfh = this.getDynamicConnectionFactory(key);
        if (cfh == null) {
            cfh = (JmsConnectionFactoryHolder) bizDriver.createChannelObject();
            this.dynamicConnectionFactories.put(key, cfh);
        }
        UserCredentialsConnectionFactoryAdapter ucConnectionFactoryAdapter = new UserCredentialsConnectionFactoryAdapter();
        ucConnectionFactoryAdapter.setTargetConnectionFactory(cfh.getConnectionFactory());
        ucConnectionFactoryAdapter.setUsername(cfh.getJmsUser());
        ucConnectionFactoryAdapter.setPassword(cfh.getJmsPassword());
        JmsTemplate embeddedJmsTemplate = null;
        if (cfh.isJms102()) {
            final SingleConnectionFactory102 scf = new SingleConnectionFactory102(ucConnectionFactoryAdapter, false);
            embeddedJmsTemplate = new JmsTemplate102(scf, false);
        } else {
            final SingleConnectionFactory scf = new SingleConnectionFactory(ucConnectionFactoryAdapter.createConnection());
            embeddedJmsTemplate = new JmsTemplate(scf);
        }
        DestinationResolver dr = cfh.getDestionationResolver();
        if (dr != null) {
            embeddedJmsTemplate.setDestinationResolver(dr);
        }
        jmsTemplateHolder = new JmsTemplateHolder(embeddedJmsTemplate);
        transactionContext.setTransactionalChannel(key, jmsTemplateHolder);
        return jmsTemplateHolder;
    }
