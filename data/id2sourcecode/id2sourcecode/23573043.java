    public double getHeight(double x, double y, int level) {
        double height = -1.0;
        String errorMessage = "";
        try {
            URL url = new URL(serviceEndPoint + "?" + "request=getHeight" + "&coordinate=" + x + "," + y);
            if (Navigator.isVerbose()) System.out.println("ElevationQueryService url " + url);
            URLConnection urlc = url.openConnection();
            urlc.setReadTimeout(Navigator.TIME_OUT);
            urlc.connect();
            InputStream is = urlc.getInputStream();
            byte[] buffer = new byte[200];
            int length = is.read(buffer);
            String buffer_s = new String(buffer);
            String zs = buffer_s.substring(0, length);
            height = Double.parseDouble(zs);
        } catch (Exception e) {
            e.printStackTrace();
            errorMessage += "<p>Error occured while connecting to ElevationQueryService</p>";
        }
        if (!errorMessage.equals("")) {
            System.out.println("\nerrorMessage: " + errorMessage + "\n\n");
            JLabel label1 = new JLabel("<html><head><style type=\"text/css\"><!--.Stil2 {font-size: 10px;font-weight: bold;}--></style></head><body><span class=\"Stil2\">Error</span></body></html>");
            JLabel label2 = new JLabel("<html><head><style type=\"text/css\"><!--.Stil2 {font-size: 10px;font-weight: normal;}--></style></head><body><span class=\"Stil2\">" + "<br>" + errorMessage + "<br>" + "<p>please check Java console. If problem persits, please report to system manager</p>" + "</span></body></html>");
            Object[] objects = { label1, label2 };
            JOptionPane.showMessageDialog(null, objects, "Error Message", JOptionPane.ERROR_MESSAGE);
        }
        return height;
    }
