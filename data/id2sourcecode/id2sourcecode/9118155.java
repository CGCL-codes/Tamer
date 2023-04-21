    public VAImagePanel() {
        super();
        Dimension d = new Dimension(200, 400);
        setBackground(Color.white);
        setBorder(new BevelBorder(BevelBorder.LOWERED));
        InputStream imgStream = VAGlobals.BASE_CLASS.getResourceAsStream("/" + VAGlobals.IMAGE);
        if (imgStream == null) {
            setPreferredSize(d);
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
                ImageIcon image = new ImageIcon(dataStream.toByteArray());
                dataStream.close();
                Insets i = getInsets();
                image.setImage(image.getImage().getScaledInstance(d.width - i.left - i.right, d.height - i.top - i.bottom, Image.SCALE_SMOOTH));
                setIcon(image);
                setPreferredSize(d);
                setMaximumSize(d);
            } catch (IOException ex) {
                setPreferredSize(d);
            }
        }
    }
