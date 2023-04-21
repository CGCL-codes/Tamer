    public static PredictionData deserialize(File dbFile) throws IOException, DataFileException {
        if ((dbFile == null) || (!dbFile.exists())) {
            return null;
        }
        FileInputStream fis = new FileInputStream(dbFile);
        CsvReader reader = new CsvReader(fis);
        String[] values = reader.getNextValidLine();
        int peptideCount = Integer.parseInt(values[0]);
        int initialCapacity = 5 * peptideCount / 4;
        int alleleCount = values.length - 1;
        String[] alleles = new String[alleleCount];
        for (int i = 0; i < alleleCount; i++) {
            alleles[i] = values[i + 1];
        }
        PredictionData result = new PredictionData(alleles, initialCapacity);
        while ((values = reader.getNextValidLine()) != null) {
            if (values.length != alleleCount + 1) {
                throw new DataFileException("Error at line " + reader.getLineNumber() + " of file " + dbFile.getCanonicalPath() + ": incorrect number of values, expected " + (alleleCount + 1) + ", found " + values.length);
            }
            String peptide = values[0];
            float[] predValues = new float[alleles.length];
            for (int i = 0; i < alleleCount; i++) {
                predValues[i] = PredictionData.NO_PREDICTION;
                String valueString = values[i + 1];
                if ((valueString != null) && (valueString.length() > 0)) {
                    try {
                        predValues[i] = Float.parseFloat(valueString);
                    } catch (Exception e) {
                        throw new DataFileException("Error at line " + reader.getLineNumber() + " of file " + dbFile.getCanonicalPath() + ": value for " + alleles[i] + " is not a valid number. Value found: " + valueString);
                    }
                }
            }
            result.setPredictions(peptide, predValues);
        }
        fis.close();
        return result;
    }
