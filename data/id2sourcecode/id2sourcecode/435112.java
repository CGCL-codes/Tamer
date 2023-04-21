    private void loadFile() {
        try {
            File newfile = new File(filename);
            BufferedReader data = null;
            if (!newfile.exists()) {
                try {
                    InputStream st = this.getClass().getResourceAsStream("/configuration.xml");
                    data = new BufferedReader(new InputStreamReader(st));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                BufferedWriter out;
                out = new BufferedWriter(new FileWriter(filename));
                String readln = data.readLine();
                while (readln != null) {
                    out.write(readln);
                    System.out.println(readln);
                    readln = data.readLine();
                }
                out.close();
            }
            parseXmlFile(new File(filename));
            parseDocument();
        } catch (Exception e) {
            System.out.println("Failed to load file");
            e.printStackTrace();
        }
    }
