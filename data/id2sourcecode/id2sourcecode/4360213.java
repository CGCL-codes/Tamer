    public void write(Date now, Card card, String title, String description, int threadNum) throws IOException {
        String emailAddress = "";
        String displayName = "";
        if (card != null) {
            emailAddress = card.getEmailAddress();
            displayName = card.getDisplayName();
        }
        csvWriter.writeLine(dateFormatter.format(now), emailAddress, displayName, title, description, String.valueOf(threadNum));
        csvWriter.flush();
    }
