    public static void main(String[] args) {
        try {
            String input = args[0];
            String output = args[1];
            boolean outputIsJar = output.endsWith(".jar");
            boolean outputIsWar = output.endsWith(".war");
            boolean outputIsEar = output.endsWith(".ear");
            boolean outputIsZip = output.endsWith(".zip");
            DataEntryWriter writer = new DirectoryWriter(new File(output), outputIsJar || outputIsWar || outputIsEar || outputIsZip);
            if (!outputIsJar) {
                DataEntryWriter zipWriter = new JarWriter(writer);
                if (outputIsZip) {
                    writer = zipWriter;
                } else {
                    writer = new FilteredDataEntryWriter(new DataEntryParentFilter(new DataEntryNameFilter(new ExtensionMatcher(".zip"))), zipWriter, writer);
                }
                DataEntryWriter warWriter = new JarWriter(writer);
                if (outputIsWar) {
                    writer = warWriter;
                } else {
                    writer = new FilteredDataEntryWriter(new DataEntryParentFilter(new DataEntryNameFilter(new ExtensionMatcher(".war"))), warWriter, writer);
                }
            }
            DataEntryWriter jarWriter = new JarWriter(writer);
            if (outputIsJar) {
                writer = jarWriter;
            } else {
                writer = new FilteredDataEntryWriter(new DataEntryParentFilter(new DataEntryNameFilter(new ExtensionMatcher(".jar"))), jarWriter, writer);
            }
            DataEntryReader reader = new DataEntryCopier(writer);
            boolean inputIsJar = input.endsWith(".jar");
            boolean inputIsWar = input.endsWith(".war");
            boolean inputIsZip = input.endsWith(".zip");
            DataEntryReader jarReader = new JarReader(reader);
            if (inputIsJar) {
                reader = jarReader;
            } else {
                reader = new FilteredDataEntryReader(new DataEntryNameFilter(new ExtensionMatcher(".jar")), jarReader, reader);
                DataEntryReader warReader = new JarReader(reader);
                if (inputIsWar) {
                    reader = warReader;
                } else {
                    reader = new FilteredDataEntryReader(new DataEntryNameFilter(new ExtensionMatcher(".war")), warReader, reader);
                }
                DataEntryReader zipReader = new JarReader(reader);
                if (inputIsZip) {
                    reader = zipReader;
                } else {
                    reader = new FilteredDataEntryReader(new DataEntryNameFilter(new ExtensionMatcher(".zip")), zipReader, reader);
                }
            }
            DirectoryPump directoryReader = new DirectoryPump(new File(input));
            directoryReader.pumpDataEntries(reader);
            writer.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
