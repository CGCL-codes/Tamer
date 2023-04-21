    public void preShowChange() {
        context.getShow().getDimmers().removeNameListener(this);
        context.getShow().getChannels().removeNameListener(this);
    }
