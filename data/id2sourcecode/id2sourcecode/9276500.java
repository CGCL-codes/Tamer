    @Override
    public JToolTip createToolTip() {
        String strURL = "http://1upclan.info/serverstatus/servers2/maps/q3a/" + this.map + ".jpg";
        URL url = null;
        boolean flag = false;
        try {
            url = new URL(strURL);
            String type = url.openConnection().getContentType();
            if (type.equals("image/jpeg")) {
                flag = true;
            }
        } catch (MalformedURLException mue) {
            flag = false;
        } catch (IOException ioe) {
            flag = false;
        }
        if (flag) {
            JToolTipWithIcon tip = new JToolTipWithIcon(new ImageIcon(url));
            tip.setComponent(this);
            return tip;
        } else {
            JToolTipWithIcon tip = new JToolTipWithIcon(new ImageIcon(getClass().getResource("/modrcon/resources/maps/default_ut.jpg")));
            tip.setComponent(this);
            return tip;
        }
    }
