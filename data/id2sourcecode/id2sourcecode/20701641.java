    public boolean loadFile(String inpfile) {
        vColumn.clear();
        Hashtable hData = new Hashtable();
        String colGreater = ">COLUMN=";
        String delimiters = "=\n\r\t";
        char greater = '>';
        char tab = '\t';
        String ex = "ex";
        String emptyString = "";
        try {
            BufferedReader in = null;
            if (inpfile.indexOf("http://") >= 0) {
                URL url = null;
                url = new URL(inpfile);
                URLConnection conn = url.openConnection();
                conn.setUseCaches(false);
                InputStreamReader is = new InputStreamReader(conn.getInputStream());
                in = new BufferedReader(is);
            } else {
                in = new BufferedReader(new FileReader(inpfile));
            }
            String pline = null;
            while ((pline = in.readLine()) != null) {
                int nLen = pline.length();
                if (nLen == 0) continue;
                if (pline.charAt(0) == greater) {
                    if (pline.indexOf(colGreater) >= 0) {
                        StringTokenizer stk = new StringTokenizer(pline, delimiters);
                        int n = stk.countTokens();
                        if (n >= 2) {
                            String s0 = stk.nextToken();
                            String s1 = stk.nextToken();
                            vColumn.add(s1);
                        }
                    }
                } else {
                    int nColm = vColumn.size();
                    if (nColm <= 0) {
                        System.out.println("less than 0 nColm");
                        continue;
                    }
                    if (nLen == 0) continue;
                    String[] tokens = tabPattern.split(pline);
                    if (tokens.length < 5) {
                        System.out.println("less than 5 vTok");
                        continue;
                    }
                    OmicElementContainer gene = new OmicElementContainer();
                    gene.setId(gene.getNewId());
                    if (tokens[0].length() > 0) {
                        gene.setName(tokens[0]);
                    }
                    if (tokens[1].length() > 0) {
                        gene.setNote(tokens[1]);
                    }
                    if (tokens[2].length() > 0) {
                        gene.setAttribute("clusterX", tokens[2]);
                    }
                    if (tokens[3].length() > 0) {
                        gene.setAttribute("clusterY", tokens[3]);
                    }
                    for (int i = 0; i < nColm; i++) {
                        if (tokens[i + 5].length() > 0) {
                            AmountContainer amount = new AmountContainer();
                            amount.setRefId(ex + i);
                            float value = Float.parseFloat(tokens[i + 5]);
                            amount.setValue(Float.toString(value));
                            gene.addAmountContainer(amount);
                        }
                    }
                    if (tokens[0].length() > 0 && !hData.containsKey(tokens[0])) {
                        vOmicElement.add(gene);
                        hData.put(tokens[0], gene);
                    }
                }
            }
            in.close();
        } catch (MalformedURLException mfe) {
            System.out.println("MalformedURLException");
            return false;
        } catch (IOException ioe) {
            System.out.println("IOException");
            return false;
        }
        return true;
    }
