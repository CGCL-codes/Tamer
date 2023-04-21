    public static Version getLatest() throws IOException {
        Version latest = null;
        try {
            URL serurl = new URL(getCurrent().home + "version.ser");
            ObjectInputStream in = new ObjectInputStream(serurl.openStream());
            latest = (Version) in.readObject();
            in.close();
        } catch (MalformedURLException murle) {
        } catch (ClassNotFoundException cnfe) {
        }
        return latest;
    }
