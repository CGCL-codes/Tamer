    public void returnState(byte[] state, String state_id) {
        String my_id = id;
        if (state_id != null) my_id += "::" + state_id;
        mux.getChannel().returnState(state, my_id);
    }
