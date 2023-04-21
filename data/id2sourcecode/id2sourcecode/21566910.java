    private ImageIcon getImageIcon(int size, double meter_size, double x_min, double y_min) {
        ImageIcon imageIcon = null;
        try {
            meta.extent_x1 = x_min;
            meta.extent_y1 = y_min;
            meta.extent_x2 = meta.extent_x1 + meter_size;
            meta.extent_y2 = meta.extent_y1 + meter_size;
            meta.image_width = size;
            meta.image_height = size;
            URL url = createMapRequestURL();
            if (Navigator.isVerbose()) System.out.println("map " + url);
            if (url != null) {
                HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                urlc.setReadTimeout(100000);
                InputStream is = urlc.getInputStream();
                is.close();
                imageIcon = new ImageIcon(url);
                imageIcon.setDescription(x_min + "," + y_min);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imageIcon;
    }
