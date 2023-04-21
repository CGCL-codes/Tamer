    public void run() {
        try {
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            Osm2Model osm = new Osm2Model();
            osm.progress.connect(this, "progress(int)");
            osm.newEdge.connect(app, "addEdgeItem(Edge)");
            osm.newNode.connect(app, "addNodeItem(Node)");
            osm.newPOI.connect(app, "addPOIItem(PointOfInterest)");
            osm.parseFile(con.getInputStream(), con.getContentLength());
            done.emit();
        } catch (Exception e) {
            failed.emit();
        }
    }
