    @Override
    public void digest(AgentModelBind agentModelBind) {
        flowDiagramModelPart.setAgentName(agentModelBind.getFlowName());
        flowDiagramModelPart.setAgentComment(agentModelBind.getComment());
        flowDiagramModelPart.setAgentImports(agentModelBind.getImports());
        flowDiagramModelPart.setAgentInterfaces(agentModelBind.getInterfaces());
        flowDiagramModelPart.setAgentParentClassName(agentModelBind.getParentClassName());
        super.digest(agentModelBind);
    }
