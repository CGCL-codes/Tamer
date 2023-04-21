    public XuiImagePanel() {
        super();
        setBackground(Color.white);
        setForeground(new Color(255, 224, 192));
        InputStream imgStream = VAGlobals.BASE_CLASS.getResourceAsStream("/" + VAGlobals.IMAGE);
        if (imgStream == null) {
        } else {
            ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
            try {
                byte[] buf = new byte[1024];
                int read = imgStream.read(buf, 0, buf.length);
                while (read > 0) {
                    dataStream.write(buf, 0, read);
                    read = imgStream.read(buf, 0, buf.length);
                }
                imgStream.close();
                JLabel img = new JLabel(new ImageIcon(dataStream.toByteArray()));
                dataStream.close();
                add(img);
            } catch (IOException ex) {
            }
        }
    }
