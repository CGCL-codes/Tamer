    @Override
    public void parse() throws IOException, DocumentException {
        URL url = new URL(this.XMLAddress);
        URLConnection con = url.openConnection();
        BufferedReader bStream = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String str;
        while ((str = bStream.readLine()) != null) {
            String[] tokens = str.split(",");
            ResultUnit unit = new ResultUnit(tokens[1], Float.valueOf(tokens[3]), Integer.valueOf(tokens[2]));
            this.set.add(unit);
        }
    }
