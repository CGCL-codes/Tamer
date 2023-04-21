    public ResponseEntity(Request request, Entity entity, Monitor monitor) {
        this.support = new Conversation(request, this);
        this.buffer = new Accumulator(support, entity, monitor);
        this.channel = entity.getChannel();
        this.sender = channel.getSender();
        this.header = entity.getHeader();
    }
