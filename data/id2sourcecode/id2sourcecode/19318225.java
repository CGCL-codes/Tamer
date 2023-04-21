    public static synchronized transferFromIdAut getInstance() {
        if (instance == null) instance = new transferFromIdAut();
        return instance;
    }
