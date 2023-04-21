    public static void downloadFile() throws IOException {
        URL google = new URL("http://ilias-userimport.googlecode.com/files/IUI_1.0.0.jar");
        ReadableByteChannel rbc = Channels.newChannel(google.openStream());
        FileOutputStream fos = new FileOutputStream("IUI_1.0.0.jar");
        fos.getChannel().transferFrom(rbc, 0, 1 << 24);
    }
