    public ChangePermissions(String name, byte[] extra_information, ResourceSet resources, String target, boolean read, boolean write, boolean execute) {
        super(name, extra_information, resources);
        this.target = target;
        this.read = read;
        this.write = write;
        this.execute = execute;
    }
