    private void findTransitiveDependencies(String libPath, LibrarySet<String> libraryEntries) {
        LibrarySet<String> clazzSet = new LibrarySet<String>();
        for (LibrarySet<String>.Entry<String> library : libraryEntries.entrySet()) {
            try {
                JarFile jarFile = new JarFile(library.toString());
                LibrarySet<String>.Entry<String>[] clazzes = library.getClassReferences().toArray(new LibrarySet.Entry[0]);
                for (int i = 0; i < clazzes.length; i++) {
                    LibrarySet.Entry clazzEntry = clazzes[i];
                    InputStream clazzStream = jarFile.getInputStream(jarFile.getEntry(clazzEntry.toString()));
                    clazzSet.addAll(poolReader.getClassesInUse(clazzStream));
                }
            } catch (IOException e) {
                writer.println("Error: Error while reading " + library + ", skipping the entry.");
            }
        }
        LibrarySet<String> transitiveCodeDeps = findCodeDependencies(libPath, clazzSet);
        transitiveCodeDeps.removeAll(libEntries);
        transitiveCodeDeps.removeAll(transitiveEntries);
        if (transitiveCodeDeps.entrySet().size() > 0) {
            transitiveEntries.addAll(transitiveCodeDeps);
            findTransitiveDependencies(libPath, transitiveCodeDeps);
        }
    }
