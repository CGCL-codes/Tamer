    private void publishBulkItems() {
        String message = "";
        int count = 0;
        for (ItemIF item : recentItems) {
            if (count > BULK_SIZE) {
                break;
            }
            message += "● " + item.getTitle() + " [ " + item.getChannel().getTitle() + " ]\n" + item.getLink().toString() + "\n\n";
            count++;
        }
        publish(message);
    }
