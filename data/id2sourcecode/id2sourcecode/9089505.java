    @Override
    public ServiceInvoker<?> getServiceInstance(RemotingMessage request) throws ServiceException {
        String messageType = request.getClass().getName();
        String destinationId = request.getDestination();
        GraniteContext context = GraniteContext.getCurrentInstance();
        Destination destination = context.getServicesConfig().findDestinationById(messageType, destinationId);
        if (destination == null) throw new ServiceException("No matching destination: " + destinationId);
        Destination d = destination;
        if (destination.getProperties().get("source").equals("*")) {
            Map<String, Serializable> propertiesMap = new HashMap<String, Serializable>();
            propertiesMap.put("source", request.getSource());
            d = new Destination(destination.getId(), destination.getChannelRefs(), propertiesMap, destination.getRoles(), destination.getAdapter());
        }
        String beanName = (String) d.getProperties().get("source");
        try {
            Object bean = springContext.getBean(beanName);
            return new NeoServiceInvoker(destination, this, bean);
        } catch (NoSuchBeanDefinitionException nexc) {
            String msg = "Spring service named '" + beanName + "' does not exist.";
            ServiceException e = new ServiceException(msg, nexc);
            throw e;
        } catch (BeansException bexc) {
            String msg = "Unable to create Spring service named '" + beanName + "'";
            ServiceException e = new ServiceException(msg, bexc);
            throw e;
        }
    }
