    public void setStartHeight(float startHeight) {
        System.out.println("Setting Start Height:\n" + this.startHeight + ", " + this.endHeight + ", " + this.height);
        this.startHeight = startHeight;
        this.height = (startHeight + endHeight) / 2;
        System.out.println(this.startHeight + ", " + this.endHeight + ", " + this.height);
    }
