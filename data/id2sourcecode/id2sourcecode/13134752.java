    @SuppressWarnings("unchecked")
    public void run(Object params) {
        Session s = null;
        try {
            s = begin();
            List<Channel> chnls = getAll(s, Channel.class);
            for (Channel chnl : chnls) {
                System.out.println(chnl.getChannelName());
            }
            commit(s);
        } catch (Exception ex) {
            ex.printStackTrace();
            if (s != null) {
                try {
                    rollback(s);
                } catch (Exception innerex) {
                }
            }
        }
    }
