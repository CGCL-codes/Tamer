    public void simulate(IProgressMonitor monitor) {
        monitor.beginTask("Simulate " + network.getName(), iterations + 3);
        final int maxPacketSize = 1024;
        SimulateSettingsImpl simulateSettings = new SimulateSettingsImpl(maxPacketSize);
        monitor.subTask("Initialize channels collection");
        final List<Channel> allChannels = network.getChannels();
        final int channels = allChannels.size();
        PacketHolder[] channelInputs = new PacketHolder[channels];
        PacketHolder[] channelOutputs = new PacketHolder[channels];
        final ChannelBehavior[] channelBehaviours = new ChannelBehavior[channels];
        for (int i = 0; i < channels; i++) {
            final Channel channel = allChannels.get(i);
            channelInputs[i] = new PacketHolderImpl();
            channelOutputs[i] = new PacketHolderImpl();
            final ChannelBehavior channelBehaviour = channelBehaviorFactory.get(channel);
            channelBehaviour.init(simulateSettings);
            channelBehaviours[i] = channelBehaviour;
        }
        monitor.internalWorked(1);
        monitor.subTask("Create nodes buffers");
        final List<Node> allNodes = network.getNodes();
        final int nodes = allNodes.size();
        int maxStoT = 4;
        int minStoT = 2;
        final int maxBuffer = maxStoT - minStoT;
        PacketHolder[][] nodeStorages = new PacketHolder[nodes][maxBuffer];
        for (PacketHolder[] nodeStorage : nodeStorages) {
            for (int i = 0; i < nodeStorage.length; i++) {
                nodeStorage[i] = new PacketHolderImpl();
            }
        }
        monitor.internalWorked(1);
        monitor.subTask("Initialize nodes collection...");
        PacketHolder[][] nodeInputs = new PacketHolder[nodes][];
        PacketHolder[][] nodeOutputs = new PacketHolder[nodes][];
        final NodeBehavior[] nodeBehaviors = new NodeBehavior[nodes];
        for (int i = 0; i < nodes; i++) {
            final Node node = allNodes.get(i);
            final List<Channel> inputChannels = node.getInputChannels();
            final PacketHolder[] inputs = new PacketHolderImpl[inputChannels.size()];
            for (int c = 0; c < inputChannels.size(); c++) {
                final Channel channel = inputChannels.get(c);
                final int index = allChannels.indexOf(channel);
                inputs[c] = channelOutputs[index];
            }
            nodeInputs[i] = inputs;
            final List<Channel> outputChannels = node.getOutputChannels();
            final PacketHolder[] outputs = new PacketHolderImpl[outputChannels.size()];
            for (int c = 0; c < outputChannels.size(); c++) {
                final Channel channel = outputChannels.get(c);
                final int index = allChannels.indexOf(channel);
                outputs[c] = channelInputs[index];
            }
            nodeOutputs[i] = outputs;
            NodeBehavior nodeBehavior = nodeBehaviorFactory.getNodeBehavoir(node);
            nodeBehavior.init(simulateSettings);
            nodeBehaviors[i] = nodeBehavior;
        }
        monitor.internalWorked(1);
        monitor.subTask("Initialize observers collection...");
        final Observer[] observers = new ObserverImpl[nodes];
        for (int n = 0; n < nodes; n++) {
            observers[n] = new ObserverImpl();
        }
        monitor.internalWorked(1);
        monitor.subTask("Simulating...");
        for (int i = 0; i < iterations; i++) {
            for (int n = 0; n < nodes; n++) {
                final NodeBehavior nodeBehavior = nodeBehaviors[n];
                final Observer observer = observers[n];
                final PacketHolder[] nodeStorage = nodeStorages[n];
                final PacketHolder[] inputs = nodeInputs[n];
                final PacketHolder[] outputs = nodeOutputs[n];
                nodeBehavior.process(observer, nodeStorage, inputs, outputs);
            }
            for (int c = 0; c < channels; c++) {
                final PacketHolder input = channelInputs[c];
                final PacketHolder output = channelOutputs[c];
                final ChannelBehavior channelBehaviour = channelBehaviours[c];
                channelBehaviour.process(input, output);
            }
            monitor.internalWorked(1);
            if (monitor.isCanceled()) break;
        }
        for (int n = 0; n < nodes; n++) {
            System.out.println(n + "\t" + observers[n].dump());
        }
    }
