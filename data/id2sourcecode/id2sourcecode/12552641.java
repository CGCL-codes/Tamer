    public void process(Writer writer) throws IOException, SAXException {
        Date now = new Date();
        writer.write("Bloglines status for " + userName + " at " + now + LINE_SEPARATOR);
        writer.write("------------------------------------------------------------------------------" + LINE_SEPARATOR);
        writer.write("Subscriptions:" + LINE_SEPARATOR);
        BloglinesSubscription subscription = service.getSubscriptions();
        printSubscription(writer, subscription, 0);
        writer.write("------------------------------------------------------------------------------" + LINE_SEPARATOR);
        writer.write("Number of unread items: " + service.getNrUnreadMessages() + LINE_SEPARATOR);
        writer.write("------------------------------------------------------------------------------" + LINE_SEPARATOR);
        writer.write("Unread items: " + LINE_SEPARATOR);
        printEntries(writer, subscription);
    }
