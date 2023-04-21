    public SVGDiagram display() {
        if (url != null) {
            try {
                uri = universe.loadSVG(url.openStream(), url.toString());
                diagram = universe.getDiagram(uri);
                bufferedImage = null;
                resize();
                previousUrl = url;
            } catch (Exception ex) {
                if (BuildProperties.DEBUG) ex.printStackTrace();
            }
        }
        return diagram;
    }
