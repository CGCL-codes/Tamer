    private String getXMLData() {
        ByteArrayOutputStream buff = new ByteArrayOutputStream();
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            DOMImplementation di = db.getDOMImplementation();
            Document doc = di.createDocument("", "running_captures", null);
            Element root = doc.getDocumentElement();
            Element elm = null;
            Element elm2 = null;
            Text text = null;
            String[] keys = (String[]) tasks.keySet().toArray(new String[0]);
            for (int index = 0; index < keys.length; index++) {
                CaptureTask task = (CaptureTask) tasks.get(keys[index]);
                ScheduleItem item = task.getScheduleItem();
                elm = doc.createElement("capture");
                elm.setAttribute("cardID", new Integer(task.getDeviceIndex()).toString());
                elm2 = doc.createElement("name");
                text = doc.createTextNode(item.getName());
                elm2.appendChild(text);
                elm.appendChild(elm2);
                elm2 = doc.createElement("start");
                text = doc.createTextNode(item.getStart().toString());
                elm2.appendChild(text);
                elm.appendChild(elm2);
                elm2 = doc.createElement("stop");
                text = doc.createTextNode(item.getStop().toString());
                elm2.appendChild(text);
                elm.appendChild(elm2);
                elm2 = doc.createElement("duration");
                text = doc.createTextNode(new Integer(item.getDuration()).toString());
                elm2.appendChild(text);
                elm.appendChild(elm2);
                elm2 = doc.createElement("channel");
                text = doc.createTextNode(item.getChannel());
                elm2.appendChild(text);
                elm.appendChild(elm2);
                String fileName = task.getCurrentFileName();
                elm2 = doc.createElement("filename");
                text = doc.createTextNode(fileName);
                elm2.appendChild(text);
                elm.appendChild(elm2);
                root.appendChild(elm);
            }
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer xformer = factory.newTransformer();
            Source source = new DOMSource(doc);
            Result result = new StreamResult(buff);
            xformer.transform(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return buff.toString();
    }
