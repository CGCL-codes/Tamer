    public static Phenotype readAnnotatedAlignment(String inputFile) throws IOException {
        String sep = "\\s+";
        BufferedReader br = Utils.getBufferedReader(inputFile);
        String[] parsedline = br.readLine().split(sep);
        String missing = "?";
        int taxaNumber = 0;
        int locusNumber = 0;
        boolean isDelimited = false;
        while (!parsedline[0].equalsIgnoreCase("<taxon_name>")) {
            if (!parsedline[0].equalsIgnoreCase("<Annotated>")) {
                if ((parsedline[0].startsWith("<") == false) || (parsedline[0].endsWith(">") == false)) {
                    throw new IllegalArgumentException("Error before or with property: " + parsedline[0]);
                }
                if ((parsedline[1].startsWith("<") == true) || (parsedline[1].endsWith(">") == true)) {
                    throw new IllegalArgumentException("Error before or with value: " + parsedline[1]);
                }
                if (parsedline[0].equalsIgnoreCase("<TRANSPOSED>")) {
                    if (parsedline[1].startsWith("N")) throw new IllegalArgumentException("Error in " + inputFile + ": The annotated format only accepts transposed data. Data not imported. ");
                }
                if (parsedline[0].equalsIgnoreCase("<TAXA_NUMBER>")) {
                    taxaNumber = Integer.parseInt(parsedline[1]);
                }
                if (parsedline[0].equalsIgnoreCase("<LOCUS_NUMBER>")) {
                    locusNumber = Integer.parseInt(parsedline[1]);
                }
                if (parsedline[0].equalsIgnoreCase("<DELIMITED_VALUES>")) {
                    if (parsedline[1].startsWith("Y")) isDelimited = true;
                }
            }
            parsedline = br.readLine().split(sep);
        }
        String[] taxaNames = new String[taxaNumber];
        if (parsedline[0].equalsIgnoreCase("<taxon_name>")) {
            for (int i = 0; i < taxaNumber; i++) taxaNames[i] = parsedline[i + 1].toUpperCase();
        } else throw new IllegalArgumentException("No taxon name section in " + inputFile + " . Data not read.");
        parsedline = br.readLine().split(sep);
        ArrayList<String> columns = new ArrayList<String>();
        int n = parsedline.length;
        int locusNameIndex = -1;
        int locusPosIndex = -1;
        for (int i = 0; i < n - 1; i++) {
            String colname = parsedline[i];
            if (!colname.startsWith("<") || !colname.endsWith(">")) throw new IllegalArgumentException("Improper format for column names in " + inputFile + ". Data not imported."); else if (colname.equalsIgnoreCase("<chromosome_number>")) columns.add(Trait.PROP_CHROMOSOME); else if (colname.equalsIgnoreCase("<genetic_position>")) columns.add(Trait.PROP_POSITION); else if (colname.equalsIgnoreCase("<locus_name>")) {
                locusNameIndex = i;
                columns.add(Trait.PROP_LOCUS);
            } else if (colname.equalsIgnoreCase("<locus_position>")) {
                locusPosIndex = i;
                columns.add(Trait.PROP_LOCUS_POSITION);
            }
        }
        int colNumber = columns.size();
        int firstValue = colNumber;
        int markerNumber = 1;
        LinkedList<Trait> traitList = new LinkedList<Trait>();
        String[] locusValues = new String[taxaNumber];
        double[] dblValues = new double[taxaNumber];
        DoubleMatrix2D genotypes = DoubleFactory2D.dense.make(taxaNumber, locusNumber);
        for (int loc = 0; loc < locusNumber; loc++) {
            parsedline = br.readLine().split(sep);
            if (isDelimited) {
                for (int t = 0; t < taxaNumber; t++) {
                    locusValues[t] = parsedline[t + firstValue];
                }
            } else {
                for (int t = 0; t < taxaNumber; t++) {
                    locusValues[t] = parsedline[firstValue].substring(t, t + 1);
                }
            }
            String traitname = "";
            if (locusNameIndex >= 0) traitname = parsedline[locusNameIndex]; else if (locusPosIndex >= 0) traitname = traitname + "." + parsedline[locusPosIndex]; else traitname = "m" + markerNumber++;
            Trait trait = makeCharacterTrait(locusValues, dblValues, traitname, Trait.TYPE_MARKER);
            for (int col = 0; col < colNumber; col++) {
                trait.setProperty(columns.get(col), parsedline[col + 1]);
            }
            traitList.add(trait);
            genotypes.viewColumn(loc).assign(dblValues);
        }
        br.close();
        return new SimplePhenotype(new SimpleIdGroup(taxaNames), traitList, genotypes);
    }
