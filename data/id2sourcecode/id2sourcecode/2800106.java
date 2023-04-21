    public static void writeThreadExecutionMessage(String readWrite, List products) {
        if (products != null && products.size() > 0) {
            StringBuilder productIds = new StringBuilder();
            for (Object product : products) {
                productIds.append(" #");
                if (product instanceof Product) {
                    productIds.append(((Product) product).getId());
                } else if (product instanceof ProductForColumnRange) {
                    productIds.append(((ProductForColumnRange) product).getId());
                } else {
                    productIds.append("?");
                }
            }
            writeThreadExecutionMessage(readWrite, productIds.toString());
        }
    }
