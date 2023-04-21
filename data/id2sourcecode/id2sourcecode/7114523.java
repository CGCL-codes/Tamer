    private void copyImageIfNotExist(String string) {
        File destination = RailsUIPlugin.getInstance().getStateLocation().append(new Path(string)).toFile();
        if (destination.exists() && destination.isFile()) return;
        URL url = FileLocator.find(RailsUIPlugin.getInstance().getBundle(), new Path("html/img/" + string), null);
        FileOutputStream stream = null;
        try {
            byte[] image = Util.getInputStreamAsByteArray(url.openStream(), -1);
            stream = new FileOutputStream(destination);
            stream.write(image);
        } catch (IOException e) {
            RailsUILog.log(e);
        } finally {
            try {
                if (stream != null) stream.close();
            } catch (IOException e) {
            }
        }
    }
