    private void copyZip(ZipInputStream zin, org.apache.tools.zip.ZipOutputStream out, List<String> files) throws IOException {
        java.util.zip.ZipEntry zentry;
        if (!alreadyWrittenFiles.containsKey(out)) {
            alreadyWrittenFiles.put(out, new HashSet<String>());
        }
        HashSet<String> currentSet = alreadyWrittenFiles.get(out);
        while ((zentry = zin.getNextEntry()) != null) {
            String currentName = zentry.getName();
            String testName = currentName.replace('/', '.');
            testName = testName.replace('\\', '.');
            if (files != null) {
                Iterator<String> i = files.iterator();
                boolean founded = false;
                while (i.hasNext()) {
                    String doInclude = i.next();
                    if (testName.matches(doInclude)) {
                        founded = true;
                        break;
                    }
                }
                if (!founded) {
                    continue;
                }
            }
            if (currentSet.contains(currentName)) {
                continue;
            }
            try {
                org.apache.tools.zip.ZipEntry newEntry = new org.apache.tools.zip.ZipEntry(currentName);
                long fileTime = zentry.getTime();
                if (fileTime != -1) {
                    newEntry.setTime(fileTime);
                }
                out.putNextEntry(newEntry);
                PackagerHelper.copyStream(zin, out);
                out.closeEntry();
                zin.closeEntry();
                currentSet.add(currentName);
            } catch (ZipException x) {
            }
        }
    }
