    public void setEndHeight(float endHeight) {
        System.out.println("Setting End Height:\n" + this.startHeight + ", " + this.endHeight + ", " + this.height);
        this.endHeight = endHeight;
        this.height = (startHeight + endHeight) / 2;
        System.out.println(this.startHeight + ", " + this.endHeight + ", " + this.height);
    }
