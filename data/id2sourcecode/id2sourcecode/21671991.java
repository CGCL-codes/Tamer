    public void addGeneration(Innovations i, Generation g, FitnessScores fitness) {
        PersistedGeneration gp = new PersistedGeneration(g, i, fitness);
        File outputFile = new File(getDirectory(), g.getGenerationNumber() + FILE_EXTENSION);
        ObjectOutputStream oos = null;
        ZipOutputStream zos = null;
        try {
            if (useCompression) {
                zos = new ZipOutputStream(new FileOutputStream(outputFile));
                zos.putNextEntry(new ZipEntry("PersistedGeneration.neat"));
                oos = new ObjectOutputStream(zos);
                oos.writeObject(gp);
                zos.closeEntry();
            } else {
                oos = new ObjectOutputStream(new FileOutputStream(outputFile));
                oos.writeObject(gp);
            }
        } catch (FileNotFoundException e) {
            throw new IllegalStateException("Could not write generation " + g.getGenerationNumber() + " to disk.", e);
        } catch (IOException e) {
            throw new IllegalStateException("Could not write generation " + g.getGenerationNumber() + " to disk.", e);
        } finally {
            try {
                if (oos != null) {
                    oos.close();
                }
                if (zos != null) {
                    zos.close();
                }
            } catch (IOException e) {
            }
        }
    }
