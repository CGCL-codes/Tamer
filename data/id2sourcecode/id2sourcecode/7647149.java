    public int primeCount(FileInputStream primeBuf) {
        if (!isCacheActive) {
            try {
                primeBuf.getChannel().position(0);
                int size = (int) primeBuf.getChannel().size();
                int count = size / SIZE_OF_INT;
                return count;
            } catch (IOException e) {
                return 0;
            }
        } else {
            return tree.size() / 4;
        }
    }
