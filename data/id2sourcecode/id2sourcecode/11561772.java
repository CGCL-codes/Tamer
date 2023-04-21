    public MapTile(int orientation, float startHeight, float endHeight) {
        this.orientation = orientation;
        this.startHeight = startHeight;
        this.endHeight = endHeight;
        this.height = (startHeight + endHeight) / 2;
        this.selected = false;
        this.fieldLocation = new Point();
        this.myColor = TOP_COLOR;
    }
