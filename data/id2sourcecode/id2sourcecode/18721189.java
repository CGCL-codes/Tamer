    public void writeBanner() {
        try {
            FileReader banner = new FileReader(BANNER_FILE);
            while (banner.ready()) {
                out_.write(banner.read());
            }
        } catch (FileNotFoundException ex) {
            System.out.println("ClientSearch.writeBanner(): " + ex);
        } catch (IOException ex) {
            System.out.println("ClientSearch.writeBanner(): " + ex);
        }
    }
