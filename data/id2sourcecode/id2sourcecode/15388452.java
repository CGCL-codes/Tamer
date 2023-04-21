    private void writeCommands() {
        if (reactor.getParameters().start()) {
            this.writer.createNewChildElement(this.setupNode, "command", "start");
        }
        if (reactor.getParameters().restart()) {
            this.writer.createNewChildElement(this.setupNode, "command", "restart").setAttribute("fromStep", String.valueOf(reactor.getParameters().getRestartFromStep()));
        }
        if (reactor.getParameters().saveElements()) {
            this.writer.createNewChildElement(this.setupNode, "command", "saveElements");
        } else {
            this.writer.createNewChildElement(this.setupNode, "command", "noElements");
        }
        if (reactor.getParameters().doBurning()) {
            String suffix = new Character((char) (88 + reactor.getParameters().getVectorBurningAxis())).toString();
            this.writer.createNewChildElement(this.setupNode, "command", "doBurning" + suffix);
        } else {
            this.writer.createNewChildElement(this.setupNode, "command", "noBurning");
        }
        if (reactor.getParameters().markPores()) {
            this.writer.createNewChildElement(this.setupNode, "command", "markPores");
        } else {
            this.writer.createNewChildElement(this.setupNode, "command", "markNoPores");
        }
        if (reactor.getParameters().calculateSurface()) {
            this.writer.createNewChildElement(this.setupNode, "command", "doContact");
        } else {
            this.writer.createNewChildElement(this.setupNode, "command", "noContact");
        }
        if (reactor.getParameters().savePixels()) {
            this.writer.createNewChildElement(this.setupNode, "command", "savePixels");
        } else {
            this.writer.createNewChildElement(this.setupNode, "command", "noPixels");
        }
        if (reactor.getParameters().redoPixels()) {
            this.writer.createNewChildElement(this.setupNode, "command", "redoPixels");
        }
        if (reactor.getParameters().getReadStep() > 0) {
            this.writer.createNewChildElement(this.setupNode, "command", "readStep").setAttribute("step", String.valueOf(reactor.getParameters().getReadStep()));
        }
        if (reactor.getParameters().doVectorBurning()) {
            this.writer.createNewChildElement(this.setupNode, "command", "doVectorBurning");
        }
    }
