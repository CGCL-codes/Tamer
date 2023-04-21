    private File getOutputFile(File inputFile, List<String> parentDirs) {
        StringBuilder nameBuilder = new StringBuilder();
        for (Iterator<String> it = parentDirs.iterator(); it.hasNext(); ) {
            nameBuilder.append(it.next() + File.separator);
        }
        nameBuilder.append(inputFile.getName());
        String completeName = nameBuilder.toString();
        String digestName;
        File digestFile;
        do {
            byte[] a = parameters.getKeyGenerationSalt();
            byte[] b = completeName.getBytes();
            byte[] inputBytes = new byte[a.length + b.length];
            System.arraycopy(a, 0, inputBytes, 0, a.length);
            System.arraycopy(b, 0, inputBytes, a.length, b.length);
            byte[] outputBytes = nameDigester.digest(inputBytes);
            digestName = HexRepresentation.get(outputBytes, true);
            digestFile = new File(baseDirectory + File.separator + digestName);
            completeName += File.separator;
        } while (digestFile.isFile());
        return digestFile;
    }
