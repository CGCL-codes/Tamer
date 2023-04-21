    public void drawChart(InputStream in) {
        chart = new Chart(this);
        getGraphicalViewer().setContents(chart);
        try {
            XMLDeserializer deserializer = new XMLDeserializer(in);
            if (deserializer.isValid()) {
                Node.resetNodeID();
                deserializer.addStartNode(chart);
                deserializer.addLogicNodes(chart);
                deserializer.addEndNodes(chart);
                deserializer.addArrows(chart);
                deserializer.addComments(chart);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
