    private boolean checkRemoteAccessibility(URL url) {
        try {
            url.openConnection().getContent();
            return true;
        } catch (IOException e) {
            return false;
        }
    }
