    @Override
    public String readContents() {
        try {
            String contents = "";
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                contents += line + "\n";
            }
            reader.close();
            return contents;
        } catch (FileNotFoundException e) {
            return "";
        } catch (IOException e) {
            return "";
        }
    }
