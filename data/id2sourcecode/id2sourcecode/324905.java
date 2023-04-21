    public void saveChannelMap(ByteArrayOutputStream chanMapBytes) {
        try {
            FileOutputStream fos = null;
            ObjectOutputStream oos = null;
            if (chanMapBytes == null) {
                DataStore store = DataStore.getInstance();
                String saveTo = store.getProperty("path.data") + File.separator + "ChannelMap.sof";
                fos = new FileOutputStream(saveTo);
                oos = new ObjectOutputStream(fos);
            } else {
                oos = new ObjectOutputStream(chanMapBytes);
            }
            oos.writeObject(getChannelMap());
            oos.close();
            if (fos == null) fos.close();
            System.out.println("ChannelMap.sof saved.");
        } catch (Exception e) {
            System.out.println("Problem saving ChannelMap.sof");
            e.printStackTrace();
        }
    }
