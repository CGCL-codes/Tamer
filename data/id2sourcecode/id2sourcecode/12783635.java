    @Override
    public void startUpdater(String fileName, String parameter) {
        try {
            log.info("Updating Updater");
            URL fileUrl = new URL("http://ossobook.svn.sourceforge.net/svnroot/ossobook/trunk/update/" + fileName);
            URLConnection filecon = fileUrl.openConnection();
            ReadableByteChannel rbc = Channels.newChannel(fileUrl.openStream());
            File testFile = new File(fileName);
            int size = filecon.getContentLength();
            if (testFile.length() == size) {
            } else {
                FileOutputStream fos = new FileOutputStream(fileName);
                fos.getChannel().transferFrom(rbc, 0, 1 << 24);
                fos.close();
            }
            Runtime.getRuntime().exec(new String[] { "java", "-jar", fileName, parameter });
            System.exit(0);
        } catch (IOException ex) {
            Logger.getLogger(GuiController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
