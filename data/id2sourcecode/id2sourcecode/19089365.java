    protected void buildWritePaths(Node arg0) {
        this.writeLocation = new StoreLocation(getWriteLocation());
        this.readLocations.add(0, this.writeLocation);
    }
