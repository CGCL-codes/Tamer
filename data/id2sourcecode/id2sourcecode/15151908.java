    public void play() {
        try {
            if (play != null && start != null) {
                as = constructor.newInstance(new Object[] { url.openStream() });
                start.invoke(play, new Object[] { as });
                return;
            }
            if (au != null) au.play();
        } catch (Exception ex) {
        }
    }
