    private void retry(DeliveryItem item) {
        try {
            storage.delete(item.getStoreName(), item.getReceiver());
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        myMessageManager.deliver(item.getMessage(), item.getReceiver(), item.getChannel());
    }
