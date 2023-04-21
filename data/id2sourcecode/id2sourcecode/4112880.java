    private boolean addOntoSpreadState(OntoSpreadState ontoSpreadState) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(output);
            oos.writeObject(ontoSpreadState);
            oos.close();
            output.close();
            return iterations.add(output);
        } catch (IOException e) {
            throw new OntoSpreadModelException(e, "Writing iteration " + current);
        }
    }
