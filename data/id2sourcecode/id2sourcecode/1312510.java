    @AroundInvoke
    public Object dispatchDataUpdate(InvocationContext ctx) throws java.lang.Exception {
        try {
            String topic = null;
            DataTopicParams paramsProvider = null;
            Gravity gravity = null;
            String clientId = null;
            String subscriptionId = null;
            Object target = ctx.getTarget();
            DataEnabled dataTopicAnnotation = target.getClass().getAnnotation(DataEnabled.class);
            if (dataTopicAnnotation != null) {
                topic = dataTopicAnnotation.topic();
                DataObserveParams params = null;
                Class<? extends DataTopicParams> dataTopicParamsClass = dataTopicAnnotation.params();
                if (dataTopicParamsClass != null) {
                    paramsProvider = dataTopicParamsClass.newInstance();
                    params = new DataObserveParams();
                    paramsProvider.observes(params);
                }
                GraniteContext graniteContext = GraniteContext.getCurrentInstance();
                if (graniteContext != null && graniteContext instanceof HttpGraniteContext) {
                    gravity = (Gravity) graniteContext.getApplicationMap().get("org.granite.gravity.Gravity");
                    HttpSession session = ((HttpGraniteContext) graniteContext).getSession(false);
                    if (gravity != null && session != null) {
                        @SuppressWarnings("unchecked") List<DataObserveParams> selectors = (List<DataObserveParams>) session.getAttribute("org.granite.tide.dataSelectors." + topic);
                        if (selectors == null) {
                            selectors = new ArrayList<DataObserveParams>();
                            session.setAttribute("org.granite.tide.dataSelectors." + topic, selectors);
                        }
                        String dataSelector = (String) session.getAttribute("org.granite.gravity.selector." + topic);
                        if (params != null && !DataObserveParams.containsParams(selectors, params)) {
                            StringBuilder sb = new StringBuilder("type = 'DATA' AND (");
                            selectors.add(params);
                            boolean first = true;
                            for (DataObserveParams selector : selectors) {
                                if (first) first = false; else sb.append(" OR ");
                                sb.append("(");
                                selector.append(sb);
                                sb.append(")");
                            }
                            sb.append(")");
                            session.setAttribute("org.granite.gravity.selector." + topic, sb.toString());
                        } else if (dataSelector == null) {
                            dataSelector = "type = 'UNITIALIZED'";
                            session.setAttribute("org.granite.tide.selector." + topic, dataSelector);
                        }
                        clientId = (String) session.getAttribute("org.granite.gravity.channel.clientId." + topic);
                        if (clientId != null) {
                            subscriptionId = (String) session.getAttribute("org.granite.gravity.channel.subscriptionId." + topic);
                            CommandMessage message = new CommandMessage();
                            message.setClientId(clientId);
                            message.setHeader(AsyncMessage.DESTINATION_CLIENT_ID_HEADER, subscriptionId);
                            message.setDestination(topic);
                            message.setOperation(CommandMessage.SUBSCRIBE_OPERATION);
                            message.setHeader(CommandMessage.SELECTOR_HEADER, dataSelector);
                            gravity.handleSubscribeMessage(message);
                        }
                    }
                }
            }
            Object result = ctx.proceed();
            if (topic != null && gravity != null) {
                try {
                    DataContext dataContext = DataContext.get();
                    if (!dataContext.getDataUpdates().isEmpty()) {
                        AsyncMessage message = new AsyncMessage();
                        message.setClientId(clientId);
                        message.setHeader(AsyncMessage.DESTINATION_CLIENT_ID_HEADER, subscriptionId);
                        message.setDestination(topic);
                        message.setHeader("type", "DATA");
                        if (paramsProvider != null) {
                            DataPublishParams params = new DataPublishParams();
                            for (Object[] data : dataContext.getDataUpdates()) paramsProvider.publishes(params, data[1]);
                            params.setHeaders(message);
                        }
                        message.setBody(dataContext.getDataUpdates().toArray());
                        gravity.publishMessage(gravity.getChannel(clientId), message);
                    }
                } catch (Exception e) {
                    log.error(e, "Could not dispatch data update");
                }
            }
            return result;
        } finally {
            DataContext.remove();
        }
    }
