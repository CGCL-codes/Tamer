    private Element cleanDocument(Element elem) {
        NodeList nodes = elem.getElementsByTagName("resource");
        List toRemove = new ArrayList();
        for (int i = 0; i < nodes.getLength(); i++) {
            Element n = (Element) nodes.item(i);
            String value = n.getAttribute("uri");
            URL url;
            try {
                url = new URL(new URL(prefixUrl + '/'), value);
            } catch (MalformedURLException e) {
                getLog().error("Malformed URL when creating the resource absolute URI : " + e.getMessage());
                return null;
            }
            try {
                url.openConnection().getContent();
            } catch (IOException e) {
                getLog().info("The bundle " + n.getAttribute("presentationname") + " - " + n.getAttribute("version") + " will be removed : " + e.getMessage());
                toRemove.add(n);
            }
        }
        Date d = new Date();
        if (toRemove.size() > 0) {
            System.out.println("Do you want to remove these bundles from the repository file [y/N]:");
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String answer = null;
            try {
                answer = br.readLine();
            } catch (IOException ioe) {
                getLog().error("IO error trying to read the user confirmation");
                return null;
            }
            if (answer != null && answer.trim().equalsIgnoreCase("y")) {
                for (int i = 0; i < toRemove.size(); i++) {
                    elem.removeChild((Node) toRemove.get(i));
                }
                SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss.SSS");
                d.setTime(System.currentTimeMillis());
                elem.setAttribute("lastmodified", format.format(d));
                return elem;
            } else {
                return null;
            }
        }
        return null;
    }
