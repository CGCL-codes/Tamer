    protected void mergeSortedFiles(File outFile) throws IOException {
        SortedMap<String, ElementDescriptor> sortedDescriptors = new TreeMap<String, ElementDescriptor>();
        for (int index = 0; index <= iterationNum; index++) {
            String fileName = workingDirectory.getPath() + File.separator + workingFileName + index + ".csv";
            System.out.println("Adding file to merge: " + fileName);
            File file = new File(fileName);
            BufferedReader reader = new BufferedReader(new FileReader(file));
            ElementDescriptor desc = new ElementDescriptor();
            desc.ligne = reader.readLine();
            desc.reader = reader;
            desc.fileName = fileName;
            desc.key = generateKeyFromLine(desc.ligne);
            sortedDescriptors.put(desc.key, desc);
        }
        OutputStream fos = new FileOutputStream(outFile);
        if (zipOutputFile) {
            fos = new ZipOutputStream(fos);
            ((ZipOutputStream) fos).setLevel(9);
            int index = outFile.getName().toLowerCase().indexOf(".zip");
            ZipEntry entry = new ZipEntry(outFile.getName().substring(0, index));
            ((ZipOutputStream) fos).putNextEntry(entry);
        }
        while (sortedDescriptors.size() > 0) {
            ElementDescriptor smallestDesc = getSmallestDescriptor(sortedDescriptors);
            fos.write((smallestDesc.ligne + "\n").getBytes());
            sortedDescriptors.remove(smallestDesc.key);
            smallestDesc.ligne = smallestDesc.reader.readLine();
            if (smallestDesc.ligne != null) {
                smallestDesc.key = generateKeyFromLine(smallestDesc.ligne);
                sortedDescriptors.put(smallestDesc.key, smallestDesc);
            } else {
                smallestDesc.reader.close();
                if (dropTempFiles) {
                    File descFile = new File(smallestDesc.fileName);
                    if (!descFile.delete()) System.out.println("Chunk file: '" + smallestDesc.fileName + "' could not be deleted. Still opened?"); else System.out.println("Chunk file: '" + smallestDesc.fileName + "' has been deleted properly.");
                }
            }
        }
        fos.flush();
        fos.close();
    }
