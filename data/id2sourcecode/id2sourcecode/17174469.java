    public int getChannelDefinition(int c) {
        if (cdbox == null) return c;
        return cdbox.getCn(c + 1);
    }
