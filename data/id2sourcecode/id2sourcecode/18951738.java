    public RequestEntity(Entity entity, Monitor monitor) {
        this.builder = new FormCreator(this, entity);
        this.channel = entity.getChannel();
        this.header = entity.getHeader();
        this.body = entity.getBody();
        this.entity = entity;
    }
