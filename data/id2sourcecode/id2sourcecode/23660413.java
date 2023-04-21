    public void openHandler() {
        try {
            URL url = new File(getFilename()).toURL();
            referenceManager.getPythonInterpreter().execfile(url.openStream());
            return;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            try {
                URL url = new URL(getFilename());
                referenceManager.getPythonInterpreter().execfile(url.openStream());
            } catch (MalformedURLException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        referenceManager.getPythonInterpreter().execfile(getFilename());
    }
