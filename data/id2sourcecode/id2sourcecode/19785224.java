    private void addJar(JarOutputStream destinationJar, JarFile sourceJar) throws GateException {
        try {
            Enumeration jarFileEntriesEnum = sourceJar.entries();
            JarEntry currentJarEntry = null;
            while (jarFileEntriesEnum.hasMoreElements()) {
                currentJarEntry = (JarEntry) jarFileEntriesEnum.nextElement();
                if (currentJarEntry.getName().equalsIgnoreCase("META-INF/") || currentJarEntry.getName().equalsIgnoreCase("META-INF/MANIFEST.MF")) continue;
                if (currentJarEntry.isDirectory() && directorySet.contains(currentJarEntry.getName())) continue;
                try {
                    if (currentJarEntry.isDirectory()) directorySet.add(currentJarEntry.getName());
                    destinationJar.putNextEntry(new JarEntry(currentJarEntry.getName()));
                    InputStream currentEntryStream = sourceJar.getInputStream(currentJarEntry);
                    int bytesRead = 0;
                    while ((bytesRead = currentEntryStream.read(buffer, 0, BUFF_SIZE)) != -1) destinationJar.write(buffer, 0, bytesRead);
                    currentEntryStream.close();
                    destinationJar.flush();
                    destinationJar.closeEntry();
                } catch (java.util.zip.ZipException ze) {
                    if (!currentJarEntry.isDirectory()) {
                        warning = true;
                        buggyJar = sourceJar.getName();
                        Out.prln("WARNING: Duplicate file entry " + currentJarEntry.getName() + " (this file will be discarded)..." + "It happened while adding " + sourceJar.getName() + " !\n");
                        dbgString.append(currentJarEntry.getName() + " file from " + sourceJar.getName() + " was discarded :( !\n");
                    }
                }
            }
        } catch (java.io.IOException e) {
            e.printStackTrace(Err.getPrintWriter());
        }
    }
