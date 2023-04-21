        public int read(PropertySupport dest, File repositoryBase, String Extension, String... targets) throws IOException {
            int resultCount = 0;
            final boolean itemsAreFiles = (getItemType() == PropertyRepositoryEntry.ItemType.FILE);
            File dir = new File(repositoryBase, createDirectoryName(targets));
            if (dir.isDirectory()) {
                String targetlist = null;
                String[] excludes = { getBaseFilename(), null };
                if (dynamic) excludes[1] = parentEntry.getMainEntryFilename();
                String[] files = dir.list((itemsAreFiles ? new SimpleFileFilter(Extension, excludes) : DNF));
                String[] mytargets = new String[targets.length + 1];
                System.arraycopy(targets, 0, mytargets, 0, targets.length);
                for (String fileName : files) {
                    File f = new File(dir, fileName);
                    if (itemsAreFiles) {
                        if (f.isFile()) {
                            int ppos = fileName.lastIndexOf(Extension);
                            String target = fileName.substring(0, ppos);
                            mytargets[targets.length] = target;
                            resultCount += readFile(dest, f, mytargets, false);
                            if (targetlist == null) targetlist = target; else targetlist += "," + target;
                        }
                    } else {
                        if (f.isDirectory()) {
                            String target = fileName;
                            mytargets[targets.length] = target;
                            for (PropertyRepositoryEntry subI : subEntries) {
                                PropertyEntry sub = (PropertyEntry) subI;
                                resultCount += sub.read(dest, repositoryBase, Extension, mytargets);
                            }
                            resultCount += readFile(dest, new File(f, mainEntryFilename + Extension), mytargets, false);
                            if (targetlist == null) targetlist = target; else targetlist += "," + target;
                        }
                    }
                }
                mytargets[targets.length] = null;
                if (itemsAreFiles) for (PropertyRepositoryEntry subI : subEntries) {
                    PropertyEntry sub = (PropertyEntry) subI;
                    resultCount += sub.read(dest, repositoryBase, Extension, mytargets);
                }
                if (!"".equals(baseFilename)) {
                    File bf = new File(dir, baseFilename + Extension);
                    resultCount += readFile(dest, bf, mytargets, true);
                }
                String list = createListProperty(targets);
                if (list != null) {
                    if (getConcatListProperty() != null) {
                        if (targetlist == null) targetlist = "@[" + dest.getPropertyDomain() + list + "." + getConcatListProperty() + "]"; else targetlist = targetlist + ",@[" + dest.getPropertyDomain() + list + "." + getConcatListProperty() + "]";
                    } else if (targetlist == null) targetlist = "";
                    dest.getProperties().put(dest.getPropertyDomain() + list + ".repository", targetlist);
                    if (!dest.getProperties().containsKey(dest.getPropertyDomain() + list)) dest.getProperties().put(dest.getPropertyDomain() + list, "@[" + dest.getPropertyDomain() + list + ".repository]");
                }
            }
            return resultCount;
        }
