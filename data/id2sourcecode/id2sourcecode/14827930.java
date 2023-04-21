    public String toString() {
        String result = "";
        synchronized (builder) {
            try {
                builder.beginTransaction();
                builder.reload(group);
                result = group.getTitle() + "[" + group.getChannels().size() + "]";
                builder.endTransaction();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }
