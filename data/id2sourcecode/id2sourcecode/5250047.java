    public boolean update() {
        boolean updated = writers > 0 || readers > 0;
        if (!getStatusFile().exists()) {
            writers = readers = 0;
            return updated;
        } else {
            long lastModified = statusFile.lastModified();
            if (lastUpdate >= lastModified) return false;
            lastUpdate = lastModified;
            try {
                for (String line : new ReadLineIterator(statusFile)) {
                    String[] fields = line.split("\\s+");
                    if (fields.length != 2) LOGGER.error("Skipping line %s (wrong number of fields)", line); else {
                        if (fields[1].equals("r")) readers += 1; else if (fields[1].equals("w")) writers += 1; else LOGGER.error("Skipping line %s (unkown mode %s)", fields[1]);
                        if (processMap != null) processMap.put(Integer.parseInt(fields[0]), fields[1].equals("w"));
                    }
                }
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        return true;
    }
