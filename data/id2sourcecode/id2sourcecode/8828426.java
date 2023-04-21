    @Override
    public RdfData executeAction(RdfData data) throws Exception {
        MMGOutput output = generateOutput(data);
        agent.sendMultimodalOutput(output.getContent(), output.getChannel());
        return null;
    }
