    public ContentPackage resolveItem() {
        try {
            return new ContentPackage(cp_url.openStream());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
