    protected void setup() {
        p = 0;
        sync(0);
        int i = 0;
        while (tokens.get(i).getChannel() != channel) {
            i++;
            sync(i);
        }
        p = i;
    }
