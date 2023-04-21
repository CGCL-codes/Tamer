    public StandardAnimalComponent(double largeScale, double smallScale, AnimalProxy ap) {
        super();
        this.largeScale = largeScale;
        this.smallScale = smallScale;
        animal_proxy = ap;
        try {
            URL url = new URL(ap.getPicture());
            if (url.getFile().equals("/~TDDB64/pics/pig.gif")) {
                setImage("../resources/pig.gif");
            } else {
                InputStream strm = url.openStream();
                strm.close();
                setImage(url);
            }
        } catch (Exception e) {
            setImage("../resources/bug.gif");
            System.err.println("Unable to get picture from URL.");
        }
        image_source = getImage().getSource();
        setScale(largeScale);
        try {
            URL url = new URL(ap.getSound());
            if (url.getFile().equals("/~TDDB64/sound/oink.au")) {
                URL pig_url = new URL(ClassLoader.getSystemResource(".") + "../resources/pig.au");
                if (pig_url != null) url = pig_url;
            }
            Farmyard.playSound(url);
        } catch (MalformedURLException e) {
            System.err.println("Malformed sound URL.");
        }
        final PropertiesFrame pf = new PropertiesFrame(animal_proxy);
        addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent event) {
                if ((event.getModifiers() & MouseEvent.BUTTON3_MASK) != 0) {
                    pf.show();
                    Thread th = new Thread() {

                        public void run() {
                            pf.setValues();
                        }
                    };
                    try {
                        th.start();
                    } catch (IllegalThreadStateException e) {
                    }
                }
            }
        });
    }
