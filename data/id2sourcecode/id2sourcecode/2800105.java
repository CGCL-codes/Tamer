    public static void writeThreadExecutionMessage(String readWrite, ProductForColumnRange product) {
        if (product != null) {
            writeThreadExecutionMessage(readWrite, " #" + product.getId());
        }
    }
