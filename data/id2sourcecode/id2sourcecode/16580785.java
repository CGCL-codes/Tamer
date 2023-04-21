    @ActionScriptProperty(read = true, write = true, bindable = true)
    @Column(name = "title", nullable = false, length = 50)
    public String getTitle() {
        return title;
    }
