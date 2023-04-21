    public Component makePhysicalComponent(List readers, List writers) {
        String logicalId = showIDLogical();
        this.registerComponent = new Physical(readers, writers, logicalId);
        registerComponent.setIDLogical(logicalId);
        return registerComponent;
    }
