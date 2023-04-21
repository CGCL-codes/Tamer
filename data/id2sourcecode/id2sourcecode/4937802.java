    public void send(File file) {
        notifyReciever();
        try {
            FileInputStream source = new FileInputStream(file);
            source.getChannel().transferTo(0, file.length(), channel);
            source.close();
        } catch (Exception e) {
            bt.removeSocket(address);
            throw new RuntimeException(e);
        }
    }
