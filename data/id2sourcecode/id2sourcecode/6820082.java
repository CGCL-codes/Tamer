    private boolean urlIsConnectable() {
        URL url = null;
        try {
            url = new URL((String) comboBox.getSelectedItem());
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        }
        try {
            if (url.openConnection().getContentLength() > 0) {
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
