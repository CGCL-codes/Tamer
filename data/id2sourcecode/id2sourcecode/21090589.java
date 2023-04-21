    private boolean accessAllowed(URL url) {
        try {
            url.openConnection().getInputStream();
        } catch (AccessControlException e) {
            System.out.println("Access to " + url + " is not allowed " + e);
            return false;
        } catch (IOException x) {
            System.out.println("Access to " + url + " is not allowed " + x);
            return false;
        }
        return true;
    }
