    private void copyZipWithoutExcludes(ZipInputStream zin, ZipOutputStream out, List<String> excludes) throws IOException {
        java.util.zip.ZipEntry zentry;
        if (!alreadyWrittenFiles.containsKey(out)) {
            alreadyWrittenFiles.put(out, new HashSet<String>());
        }
        HashSet<String> currentSet = alreadyWrittenFiles.get(out);
        while ((zentry = zin.getNextEntry()) != null) {
            String currentName = zentry.getName();
            String testName = currentName.replace('/', '.');
            testName = testName.replace('\\', '.');
            if (excludes != null) {
                Iterator<String> i = excludes.iterator();
                boolean skip = false;
                while (i.hasNext()) {
                    String doExclude = i.next();
                    if (testName.matches(doExclude)) {
                        skip = true;
                        break;
                    }
                }
                if (skip) {
                    continue;
                }
            }
            if (currentSet.contains(currentName)) {
                continue;
            }
            try {
                ZipEntry newEntry = new ZipEntry(currentName);
                long fileTime = zentry.getTime();
                if (fileTime != -1) {
                    newEntry.setTime(fileTime);
                }
                out.putNextEntry(newEntry);
                copyStream(zin, out);
                out.closeEntry();
                zin.closeEntry();
                currentSet.add(currentName);
            } catch (ZipException x) {
            }
        }
    }
