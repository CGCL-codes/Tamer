    public AtomItem(ItemIF src) {
        this.setId(IdGenerator.getInstance().getId());
        this.setChannel(src.getChannel());
        this.setTitle(src.getTitle());
        this.setDescription(src.getDescription());
        this.setLink(src.getLink());
        this.setCreator(src.getCreator());
        this.setSubject(src.getSubject());
        this.setDate(src.getDate());
        this.setFound(src.getFound());
        this.setUnRead(src.getUnRead());
        this.setCategories(src.getCategories());
    }
