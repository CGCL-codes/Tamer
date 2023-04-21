    public AreaFile(String source) throws AreaFileException {
        imageSource = source;
        if (imageSource.startsWith("adde://") && (imageSource.endsWith("image?") || imageSource.endsWith("imagedata?"))) {
            GetAreaGUI gag = new GetAreaGUI((Frame) null, true, "Get data", false, true);
            gag.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    imageSource = e.getActionCommand();
                }
            });
            gag.show();
        }
        try {
            af = new DataInputStream(new BufferedInputStream(new FileInputStream(imageSource), 2048));
        } catch (IOException eIO) {
            URL url;
            try {
                url = new URL(imageSource);
                URLConnection urlc = url.openConnection();
                InputStream is = urlc.getInputStream();
                af = new DataInputStream(new BufferedInputStream(is));
            } catch (IOException e) {
                fileok = false;
                throw new AreaFileException("Error opening AreaFile: " + e);
            }
            isRemote = url.getProtocol().equalsIgnoreCase("adde");
        }
        fileok = true;
        position = 0;
        readMetaData();
    }
