    public static void writeThreadExecutionMessage(String readWrite, Product product) {
        if (product != null) {
            writeThreadExecutionMessage(readWrite, " #" + product.getId());
        }
    }
