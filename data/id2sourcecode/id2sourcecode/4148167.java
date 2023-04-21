    public ArrayList parseFile(File newfile) throws IOException {
        String s;
        String[] tokens;
        URL url = newfile.toURI().toURL();
        InputStream is = url.openStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        while ((s = br.readLine()) != null) {
            tokens = s.split("\\s+");
            int nvalues = tokens.length;
            ArrayList columndata = new ArrayList();
            for (int i = 0; i < nvalues; i++) {
                if (((String) tokens[i]).length() > 0) {
                    columndata.add(new Double(Double.parseDouble(tokens[i])));
                }
            }
            data.add(columndata);
        }
        System.out.println("Matrix size is " + data.size() + " by " + ((ArrayList) data.get(0)).size());
        return data;
    }
