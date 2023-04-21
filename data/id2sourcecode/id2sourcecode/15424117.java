    private List<String> executeListDatastreams(String id) throws IOException {
        List<String> list = new LinkedList<String>();
        URL url = new URL(fedoraServer + "listDatastreams/" + id + "?xml=true");
        InputStream in = url.openStream();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int read = in.read();
        while (read != -1) {
            out.write(read);
            read = in.read();
        }
        String string = out.toString();
        string = string.replaceAll(">", ">DELIMITER");
        for (String line : string.split("DELIMITER")) {
            if (!line.startsWith("<?xml")) list.add(line);
        }
        return list;
    }
