    private void registerChannel() {
        try {
            JChannel channel = (JChannel) this.notificationBus.getChannel();
            ObjectName channelName = this.getObjectName(channel);
            MBeanServer server = MBeanServerLocator.locateMBeanServer();
            Class jmxConfigurator = ClassUtils.forName("org.jgroups.jmx.JmxConfigurator");
            Class[] parameterTypes = new Class[] { JChannel.class, MBeanServer.class, String.class, boolean.class };
            Method registerChannel = jmxConfigurator.getMethod("registerChannel", parameterTypes);
            Object[] values = new Object[] { channel, server, channelName.getCanonicalName(), Boolean.TRUE };
            registerChannel.invoke(null, values);
        } catch (Exception e) {
            if (e instanceof InvocationTargetException) {
                InvocationTargetException ite = (InvocationTargetException) e;
                Throwable cause = ite.getTargetException();
                if (cause instanceof JMException) {
                    if (logger.isInfoEnabled()) {
                        logger.info("JMX exception at start of this service registering the JChannel :: " + cause.getClass().getName());
                        logger.info("Message of this exception :: " + cause.getMessage());
                    }
                } else {
                    throw new ServiceRuntimeException(cause);
                }
            } else {
                throw new ServiceRuntimeException(e);
            }
        }
    }
