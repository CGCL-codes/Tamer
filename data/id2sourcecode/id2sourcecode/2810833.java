    private static COOS instantiate(CoosType model, COContainer container) throws Exception {
        COOS coos = new COOS();
        if (model.getName() == null) {
            throw new Exception("COOS instance has no name. Must be defined!");
        }
        coos.setName(model.getName());
        if (model.getRouter() == null) {
            throw new Exception("COOS must contain a router. Must be defined!");
        }
        if (model.getRouter().getClass1() == null) {
            throw new Exception("COOS router must have a implementing class. Must be defined!");
        }
        Class<?> cl = COOSFactory.tryClass(container, model.getRouter().getClass1());
        Router router = (Router) cl.newInstance();
        Map<String, String> properties = new HashMap<String, String>();
        for (int j = 0; j < model.getRouter().getPropertyArray().length; j++) {
            PropertyType propertyType = model.getRouter().getPropertyArray()[j];
            properties.put(propertyType.getName(), propertyType.getValue());
        }
        Hashtable<String, String> properties2 = new Hashtable<String, String>(properties);
        router.setProperties(properties2);
        coos.setRouter(router);
        for (int i = 0; i < model.getRouter().getRouterprocessorArray().length; i++) {
            ProcessorType processorType = model.getRouter().getRouterprocessorArray()[i];
            if ((processorType.getName() == null) || (processorType.getClass1() == null)) {
                throw new Exception("COOS processors must have a name and a implementing class. Must be defined!");
            }
            String name = processorType.getName();
            String className = processorType.getClass1();
            boolean isShared = processorType.getShared();
            Map<String, String> props = new HashMap<String, String>();
            for (int j = 0; j < processorType.getPropertyArray().length; j++) {
                PropertyType propertyType = processorType.getPropertyArray()[j];
                props.put(propertyType.getName(), propertyType.getValue());
            }
            Class<?> processorClass = COOSFactory.tryClass(container, className);
            Processor processor = (Processor) processorClass.newInstance();
            processor.setShared(isShared);
            processor.setProperties(new Hashtable<String, String>(props));
            coos.addProcessor(name, processor);
        }
        for (int i = 0; i < model.getRouter().getPreprocessorArray().length; i++) {
            RouterprocessorType routerprocessorType = model.getRouter().getPreprocessorArray()[i];
            if (coos.getProcessor(routerprocessorType.getRouterprocessor()) == null) {
                throw new Exception("COOS processors with name: " + routerprocessorType.getRouterprocessor() + " is not defined. referenced from router preprocessors!");
            }
            router.addPreProcessor((RouterProcessor) coos.getProcessor(routerprocessorType.getRouterprocessor()));
        }
        for (int i = 0; i < model.getRouter().getPostprocessorArray().length; i++) {
            RouterprocessorType routerprocessorType = model.getRouter().getPostprocessorArray()[i];
            if (coos.getProcessor(routerprocessorType.getRouterprocessor()) == null) {
                throw new Exception("COOS processors with name: " + routerprocessorType.getRouterprocessor() + " is not defined. referenced from router postprocessors!");
            }
            router.addPostProcessor((RouterProcessor) coos.getProcessor(routerprocessorType.getRouterprocessor()));
        }
        router.addQoSClass(Link.DEFAULT_QOS_CLASS, true);
        for (int i = 0; i < model.getRouter().getQosclassArray().length; i++) {
            QosclassType qosclassType = model.getRouter().getQosclassArray()[i];
            if (qosclassType.getName() == null) {
                throw new Exception("COOS QoS types must have a name. Must be defined!");
            }
            router.addQoSClass(qosclassType.getName(), Boolean.valueOf(qosclassType.getDefault()));
        }
        for (int j = 0; j < model.getRouter().getRouteralgorithmArray().length; j++) {
            RouteralgorithmType routeralgorithmType = model.getRouter().getRouteralgorithmArray()[j];
            String algName = routeralgorithmType.getName();
            if ((algName == null) || (routeralgorithmType.getClass1() == null)) {
                throw new Exception("COOS router algorithms must have a name and a implementing class. Must be defined!");
            }
            String className = routeralgorithmType.getClass1();
            Class<?> algorithmClass = COOSFactory.tryClass(container, className);
            Map<String, String> props = new HashMap<String, String>();
            for (int k = 0; k < routeralgorithmType.getPropertyArray().length; k++) {
                PropertyType propertyType = routeralgorithmType.getPropertyArray()[k];
                props.put(propertyType.getName(), propertyType.getValue());
            }
            RoutingAlgorithm algorithm = (RoutingAlgorithm) algorithmClass.newInstance();
            algorithm.setProperties(new Hashtable<String, String>(props));
            coos.addRoutingAlgorithm(algName, algorithm);
        }
        boolean defaultSegmentDefined = false;
        for (int i = 0; i < model.getRouter().getSegmentArray().length; i++) {
            SegmentType segmentType = model.getRouter().getSegmentArray()[i];
            String segmentName = segmentType.getName();
            if ((segmentName == null) || segmentName.equals("")) {
                segmentName = ".";
            }
            if (segmentName.equals(Router.LOCAL_SEGMENT) || segmentName.equals(Router.DICO_SEGMENT) || (segmentName.indexOf('/') != -1) || (segmentName.indexOf('\\') != -1) || (segmentName.indexOf(' ') != -1) || (segmentName.indexOf('-') != -1)) {
                throw new Exception("Invalid COOS segment: " + segmentName + " Can not contain: blanks, \\, /, - or the reserved segment 'localcoos' and 'dico'.");
            }
            String routerUuid;
            if ((segmentType.getRouteruuid() != null) && !segmentType.getRouteruuid().equals("")) {
                routerUuid = Router.ROUTER_UUID_PREFIX + segmentType.getRouteruuid();
            } else {
                routerUuid = Router.ROUTER_UUID_PREFIX + coos.getName();
            }
            if (routerUuid == null) {
                throw new Exception("COOS Segments must have a segment unique identification of the Router uuid. Must be defined!");
            }
            if (routerUuid.contains(".")) {
                throw new Exception("Router uuid cannot contain '.', The segment is explicitly declared in segment attribute.");
            }
            if (segmentName.equals(".")) {
                routerUuid = segmentName + routerUuid;
            } else {
                routerUuid = segmentName + "." + routerUuid;
            }
            UuidGenerator gen = new UuidGenerator(routerUuid);
            routerUuid = gen.generateId();
            String routerAlgorithm = segmentType.getRouteralgorithm();
            if (segmentType.getRouteralgorithm() == null) {
                throw new Exception("COOS Segments must have a router algorithm. Must be defined!");
            }
            String defaultSegmentStr = segmentType.getDefaultSegment();
            boolean defaultSegment = (defaultSegmentStr != null) && defaultSegmentStr.equalsIgnoreCase("true");
            if (defaultSegment) {
                defaultSegmentDefined = true;
            }
            RoutingAlgorithm prototypeAlg = coos.getRoutingAlgorithm(routerAlgorithm);
            if (prototypeAlg == null) {
                throw new Exception("COOS router algorithm: " + routerAlgorithm + " is not defined. Must be defined!");
            }
            RoutingAlgorithm algorithm = prototypeAlg.copy();
            if (segmentType.getLogging() != null) {
                algorithm.setLoggingEnabled(segmentType.getLogging().equalsIgnoreCase("true"));
            }
            algorithm.init(routerUuid, router);
            coos.addSegment(new RouterSegment(segmentName, routerUuid, algorithm.getAlgorithmName(), defaultSegment));
        }
        if (!defaultSegmentDefined) {
            for (RouterSegment rs : coos.getSegmentMap().values()) {
                rs.setDefaultSegment(true);
                break;
            }
        }
        router.setSegmentMappings(new Hashtable<String, RouterSegment>(coos.getSegmentMap()));
        for (int i = 0; i < model.getProcessorArray().length; i++) {
            ProcessorType processorType = model.getProcessorArray()[i];
            if ((processorType.getName() == null) || (processorType.getClass1() == null)) {
                throw new Exception("COOS processors must have a name and a implementing class. Must be defined!");
            }
            String name = processorType.getName();
            processorTypes.put(name, processorType);
            Processor processor = instantiateProcessor(processorType, container);
            if (processor.isShared()) {
                sharedProcessors.put(name, processor);
            }
            coos.addProcessor(name, processor);
        }
        for (int i = 0; i < model.getTransportArray().length; i++) {
            TransportType transportType = model.getTransportArray()[i];
            if ((transportType.getName() == null) || (transportType.getClass1() == null)) {
                throw new Exception("COOS transports must have a name and an implementing class. Must be defined!");
            }
            String name = transportType.getName();
            String className = transportType.getClass1();
            Map<String, String> props = new HashMap<String, String>();
            for (int j = 0; j < transportType.getPropertyArray().length; j++) {
                PropertyType propertyType = transportType.getPropertyArray()[j];
                if ((propertyType.getName() == null) || (propertyType.getValue() == null)) {
                    throw new Exception("COOS properties must have a name and a value. Must be defined!");
                }
                props.put(propertyType.getName(), propertyType.getValue());
            }
            Class<?> transportClass = COOSFactory.tryClass(container, className);
            Transport transport = (Transport) transportClass.newInstance();
            transport.setProperties(new Hashtable<String, String>(props));
            transport.setName(name);
            coos.addTransport(name, transport);
        }
        for (int i = 0; i < model.getChannelArray().length; i++) {
            ChannelType channelType = model.getChannelArray()[i];
            if (channelType.getName() == null) {
                throw new Exception("COOS channels must have a name. Must be defined!");
            }
            String name = channelType.getName();
            boolean init = channelType.getInit();
            boolean defaultGw = channelType.getDefaultgw();
            boolean receiveRoutingInfo = channelType.getReceiveroutinginfo();
            String transport = channelType.getTransport();
            RouterChannel channel = new RouterChannel();
            channel.setName(name);
            channel.setInit(init);
            channel.setDefaultGw(defaultGw);
            channel.setReceiveRoutingInfo(receiveRoutingInfo);
            String segment = channelType.getSegment();
            if (init && ((segment == null) || segment.equals(""))) {
                segment = ".";
            }
            if (segment != null) {
                if (coos.getRouter().getRoutingAlgorithm(segment) == null) {
                    throw new Exception("COOS channel: " + channel + " is defined to connect to segment: " + segment + " which the Coos instance: " + coos.getName() + " does not maintain.");
                }
                channel.setConnectingPartyUuid(coos.getRouter().getRoutingAlgorithm(segment).getRouterUuid());
            }
            if (transport != null) {
                channel.setTransport(coos.getTransport(transport));
            }
            if ((segment != null) && (coos.getSegment(segment) != null)) {
                channel.setRoutingAlgorithmName(coos.getSegment(segment).getRoutingAlgorithmName());
            }
            channel.setLinkManager(router);
            Link inLink = new Link();
            channel.setInLink(inLink);
            InBoundType inBoundType = channelType.getInBound();
            if (inBoundType != null) {
                for (int j = 0; j < inBoundType.getFilterArray().length; j++) {
                    FilterType filterType = inBoundType.getFilterArray()[j];
                    String processor = filterType.getProcessor();
                    ProcessorType procType = processorTypes.get(processor);
                    if (procType == null) {
                        throw new Exception("Processor " + processor + " is not declared.");
                    }
                    if (procType.getShared()) {
                        channel.getInLink().addFilterProcessor(sharedProcessors.get(processor));
                    } else {
                        channel.getInLink().addFilterProcessor(instantiateProcessor(procType, container));
                    }
                }
            }
            Link outLink = new Link();
            channel.setOutLink(outLink);
            OutBoundType outBoundType = channelType.getOutBound();
            if (outBoundType != null) {
                for (int j = 0; j < outBoundType.getFilterArray().length; j++) {
                    FilterType filterType = outBoundType.getFilterArray()[j];
                    String processor = filterType.getProcessor();
                    ProcessorType procType = processorTypes.get(processor);
                    if (procType == null) {
                        throw new Exception("Processor " + processor + " is not declared.");
                    }
                    if (procType.getShared()) {
                        channel.getOutLink().addFilterProcessor(sharedProcessors.get(processor));
                    } else {
                        channel.getOutLink().addFilterProcessor(instantiateProcessor(procType, container));
                    }
                }
                for (int j = 0; j < outBoundType.getCostArray().length; j++) {
                    CostType costType = outBoundType.getCostArray()[j];
                    outLink.setCost(costType.getName(), costType.getValue());
                }
            }
            coos.addChannel(name, channel);
        }
        DefaultChannelServer defaultChannelServer = new DefaultChannelServer();
        defaultChannelServer.setCOOSInstance(coos);
        defaultChannelServer.setLinkManager(router);
        coos.addChannelServer("default", defaultChannelServer);
        for (int i = 0; i < model.getChannelserverArray().length; i++) {
            ChannelserverType channelserverType = model.getChannelserverArray()[i];
            if ((channelserverType.getName() == null) || (channelserverType.getClass1() == null)) {
                throw new Exception("COOS channel servers must have a name and a implementing class. Must be defined!");
            }
            String name = channelserverType.getName();
            String className = channelserverType.getClass1();
            Map<String, String> props = new HashMap<String, String>();
            for (int j = 0; j < channelserverType.getPropertyArray().length; j++) {
                PropertyType propertyType = channelserverType.getPropertyArray()[j];
                if ((propertyType.getName() == null) || (propertyType.getValue() == null)) {
                    throw new Exception("COOS properties must have a name and a value. Must be defined!");
                }
                props.put(propertyType.getName(), propertyType.getValue());
            }
            Class<?> serverClass = COOSFactory.tryClass(container, className);
            ChannelServer server = (ChannelServer) serverClass.newInstance();
            server.setCOOSInstance(coos);
            server.setProperties(new Hashtable<String, String>(props));
            server.setLinkManager(router);
            Hashtable<String, RouterChannel> channelMap = new Hashtable<String, RouterChannel>();
            for (int j = 0; j < channelserverType.getChannelMappingArray().length; j++) {
                ChannelMappingType channelMappingType = channelserverType.getChannelMappingArray()[j];
                if ((channelMappingType.getUuid() == null) || (channelMappingType.getChannel() == null)) {
                    throw new Exception("COOS channel mappings must define a relation between a uuid regexp pattern and a channel.");
                }
                String uuid = channelMappingType.getUuid();
                String channel = channelMappingType.getChannel();
                if (coos.getChannel(channel) == null) {
                    throw new Exception("COOS channel: " + channel + " referenced from channel mappings is not defined.");
                }
                channelMap.put(uuid, coos.getChannel(channel));
            }
            server.setChannelMappings(channelMap);
            coos.addChannelServer(name, server);
        }
        coosInstances.put(model.getName(), coos);
        return coos;
    }
