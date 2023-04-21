    @Override
    public void setInput(IEditorInput input) {
        superSetInput(input);
        IFile file = ((IFileEditorInput) input).getFile();
        try {
            repast.simphony.agents.model.bind.AgentModelBind agentModelBind = BindingHandler.getInstance().loadFlowModel(file.getLocation().toFile());
            AgentDiagramModelPart flowDiagramModelPart = new AgentDiagramModelPart();
            DesignerModelDigester digester = new DesignerModelDigester(flowDiagramModelPart);
            digester.digest(agentModelBind);
            setFlowDiagram(flowDiagramModelPart);
            digester.getCreateCommand().execute();
        } catch (AgentBuilderRuntimeException e) {
            AgentBuilderPlugin.log(e);
            AgentBuilderPlugin.message("Problems while reading agent file: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            AgentBuilderPlugin.log(e);
            AgentBuilderPlugin.message("Problems while opening agent file: " + e.getMessage());
            e.printStackTrace();
        }
    }
