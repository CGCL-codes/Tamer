    private void saveDisk(String filename) {
        try {
            reader.writeDisk(new FileOutputStream(filename));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
