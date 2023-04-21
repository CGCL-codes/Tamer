    public ArrayList getAvailableNetworks(String wirelessInterface) {
        String result;
        ArrayList networks = new ArrayList();
        String[] line, element, part;
        result = shell.sendCommand("iwlist " + wirelessInterface + " scanning");
        line = result.split("\n");
        String[] property = { "", "" };
        for (int i = 0; i < line.length; i++) {
            element = line[i].split(":");
            for (int j = 0; j < element.length; j++) {
                if (element[j].endsWith("ESSID") || element[j].endsWith("Mode")) {
                    if (element[j].endsWith("ESSID")) {
                        property[1] = element[j + 1];
                    } else {
                        property[0] = element[j + 1];
                        networks.add(property);
                        property = (String[]) property.clone();
                    }
                }
            }
        }
        return networks;
    }
