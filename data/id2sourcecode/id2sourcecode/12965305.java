    public void start() {
        URL base = null;
        try {
            base = this.isActive() ? this.getDocumentBase() : new File(System.getProperty("user.dir")).toURI().toURL();
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        }
        try {
            URL url = new URL(base, "hueplane/" + dir + "/new.txt");
            newDataArray = DoubleArray.to3DDoubleArray(ASCIIFile.readDoubleArray(url.openStream()), 10, 10);
            url = new URL(base, "hueplane/" + dir + "/target.txt");
            targetDataArray = DoubleArray.to3DDoubleArray(ASCIIFile.readDoubleArray(url.openStream()), 10, 10);
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        normalPlot();
    }
