    public void setup(ImagePlus imp) {
        if (imp == null) {
            IJ.noImage();
            return;
        }
        this.imp = imp;
        bitDepth = imp.getBitDepth();
        ImageProcessor ip = imp.getChannelProcessor();
        IndexColorModel cm = (IndexColorModel) ip.getColorModel();
        origin = cm;
        mapSize = cm.getMapSize();
        reds = new byte[256];
        greens = new byte[256];
        blues = new byte[256];
        cm.getReds(reds);
        cm.getGreens(greens);
        cm.getBlues(blues);
        addMouseListener(this);
        addMouseMotionListener(this);
        for (int index = 0; index < mapSize; index++) c[index] = new Color(reds[index] & 255, greens[index] & 255, blues[index] & 255);
    }
