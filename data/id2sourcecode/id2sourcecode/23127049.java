    private void loadProperties(String filename) {
        try {
            File file = new File(filename);
            if (!file.exists()) return;
            BufferedReader in = new BufferedReader(new FileReader(file));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                if (inputLine.trim().length() == 0) continue;
                if (inputLine.trim().startsWith("#")) continue;
                String[] tokens = tokenize(inputLine, "=");
                setConfValues(tokens[0], (tokens.length == 2 ? tokens[1] : ""));
            }
            in.close();
        } catch (IOException e) {
            System.err.println("Could not read/write property file '" + filename + "'! " + e);
        }
    }
