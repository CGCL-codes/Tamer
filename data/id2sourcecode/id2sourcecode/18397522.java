    public boolean readPGM(URL url, int address) {
        try {
            return readPGM(url.openConnection().getInputStream(), address);
        } catch (Exception e) {
            System.out.println("Error when opening url " + url + "  " + e);
        }
        return false;
    }
