    public static boolean isMonolithStore(File store) {
        if (!store.exists()) return false;
        FileChannel channel = null;
        try {
            channel = new RandomAccessFile(store, "r").getChannel();
            AllocationTable table = new AllocationTable(channel);
            table.loadTable();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (channel != null) channel.close();
            } catch (IOException e) {
            }
        }
    }
