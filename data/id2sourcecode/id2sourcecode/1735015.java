    public String getDescriptionFromWeb() {
        String res = "";
        String line;
        res = "Name: " + name + "\n";
        if (gender != null) res += "Gender: " + gender + "\n";
        if (!universes.isEmpty()) res += "Universes: " + universes.toString().subSequence(1, universes.toString().length() - 1) + "\n";
        if (!species.isEmpty()) res += "Species: " + species.toString().substring(1, species.toString().length() - 1) + "\n";
        if (!powers.isEmpty()) res += "Powers: " + powers.toString().substring(1, powers.toString().length() - 1) + "\n";
        if (freebase_id != "") {
            try {
                URL url = new URL("http://api.freebase.com/api/trans/raw" + freebase_id);
                BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
                line = reader.readLine();
                if (line != null && line.charAt(0) < 128) {
                    res += "\nDescription:\n";
                    res += removeXMLbrackets(line) + "\n";
                    while ((line = reader.readLine()) != null) {
                        res += removeXMLbrackets(line) + "\n";
                    }
                }
                reader.close();
            } catch (IOException e) {
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return res;
    }
