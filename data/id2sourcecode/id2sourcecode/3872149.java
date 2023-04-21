    public void downloadData() throws Exception {
        String[] states = { "NSW", "NT", "QLD", "SA", "TAS", "VIC", "WA" };
        String bomurl = "ftp://ftp.bom.gov.au/anon/gen/fwo/IDYGP007.XXX.txt";
        String line;
        LinkedList locations = new LinkedList();
        for (int i = 0; i < 7; i++) {
            String urlstring = bomurl.replaceFirst("XXX", states[i]);
            System.out.println("Trying " + urlstring);
            URL url = new URL(urlstring);
            URLConnection urlc = url.openConnection();
            InputStream is = urlc.getInputStream();
            Scanner scanner = new Scanner(is);
            while (scanner.hasNextLine()) {
                line = scanner.nextLine();
                System.out.println("  got: " + line);
                if (line.length() > 100) {
                    line = states[i] + ", " + line.substring(18, 37).trim() + ": " + line.substring(50, 84).trim() + ", " + line.substring(85).trim();
                    System.out.println("  use: " + line);
                    locations.add(line);
                }
            }
        }
        String defaultLocation = Preferences.userNodeForPackage(UVData.class).get("defaultLocation", "unknown");
        removeAllElements();
        if (!locations.isEmpty()) {
            addElement("Select a location...");
            while (!locations.isEmpty()) {
                line = locations.removeFirst().toString();
                addElement(line);
                if (getLocation(line).equals(defaultLocation)) setSelectedItem(line);
            }
        }
    }
