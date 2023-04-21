    public void saveTerrain(String mapname, Vector<DrawingBean> v) throws IOException {
        String[] formats = { "jpg", "png" };
        String format;
        boolean hasFormat = false;
        if (mapname.length() > 4) {
            format = mapname.substring(mapname.length() - 3).toLowerCase();
            for (int i = 0; i < formats.length; i++) {
                if (format.equalsIgnoreCase(formats[i])) {
                    hasFormat = true;
                    break;
                }
            }
            if (!hasFormat) {
                format = formats[0];
                mapname = mapname + "." + format;
            }
        } else {
            format = formats[0];
            mapname = mapname + "." + format;
        }
        String dstpath = new java.io.File(".").getCanonicalPath() + "\\src\\maps\\" + mapname;
        File dstfile = new File(dstpath);
        BufferedImage bi = new BufferedImage(terrain_width * MULT, terrain_height * MULT, BufferedImage.TYPE_INT_RGB);
        bi.createGraphics();
        Graphics2D g = (Graphics2D) bi.getGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, terrain_width * MULT, terrain_height * MULT);
        for (int i = 0; i < v.size(); i++) {
            g.setColor(v.get(i).getColor());
            g.fillRect(v.get(i).getPoint().x * MULT, v.get(i).getPoint().y * MULT, MULT, MULT);
        }
        if (dstfile.exists()) {
            int option = JOptionPane.showOptionDialog(this, "This file already exists. Overwrite it?", "NOTIFICATION", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
            if (option == JOptionPane.YES_OPTION) {
                ImageIO.write(bi, format, dstfile);
                MapsXMLManager manager = new MapsXMLManager(mapname);
                manager.saveMap(GUIOptionManager.getMapWidthMeters(), GUIOptionManager.getMapHeightMeters(), terrain_width, terrain_height, v);
            }
        } else {
            ImageIO.write(bi, format, dstfile);
            MapsXMLManager manager = new MapsXMLManager(mapname);
            manager.saveMap(GUIOptionManager.getMapWidthMeters(), GUIOptionManager.getMapHeightMeters(), terrain_width, terrain_height, v);
        }
    }
