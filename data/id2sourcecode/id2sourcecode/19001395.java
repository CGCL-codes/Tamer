    @Override
    public String toString() {
        return this.name == null ? "NA" : this.name + ",Size: " + this.size() + ", Readers: " + this.readers + ", Writers: " + this.writers;
    }
