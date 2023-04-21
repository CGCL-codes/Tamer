    public HashMap parseFile(File newfile) throws IOException {
        String s;
        String firstWord;
        String header;
        String name = null;
        String[] tokens;
        int nvalues = 0;
        double phase = 0.0;
        double stepsize = 0.0;
        boolean readraw = false;
        URL url = newfile.toURI().toURL();
        InputStream is = url.openStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        s = br.readLine();
        tokens = s.split("\\s+");
        String numname = tokens[7];
        String[] subtoken = numname.split("#");
        name = subtoken[1];
        s = br.readLine();
        s = br.readLine();
        tokens = s.split("\\s+");
        phase = (new Double(Double.parseDouble(tokens[5]))).doubleValue();
        String step = tokens[15];
        subtoken = step.split("=");
        stepsize = (new Double(Double.parseDouble(subtoken[1]))).doubleValue();
        System.out.println("BSM is " + name + " with phase " + phase + " and stepsize " + stepsize);
        while ((s = br.readLine()) != null) {
            tokens = s.split("\\s+");
            nvalues = tokens.length;
            ArrayList columndata = new ArrayList();
            for (int i = 0; i < nvalues; i++) {
                if (((String) tokens[i]).length() > 0) {
                    columndata.add(new Double(Double.parseDouble(tokens[i])));
                }
            }
            bsmdata.add(columndata);
        }
        System.out.println("Lengths are " + bsmdata.size() + "  " + ((ArrayList) bsmdata.get(0)).size());
        HashMap data = new HashMap();
        data.put("name", name);
        data.put("phase", phase);
        data.put("stepsize", stepsize);
        data.put("data", bsmdata);
        return data;
    }
