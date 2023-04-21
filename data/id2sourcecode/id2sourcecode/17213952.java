    public static void main(String[] s) {
        boolean fileListFromStdin = false;
        char filenameSeparator = ' ';
        insist(s.length >= 1);
        if (s[0].equals("-") || s[0].equals("-0")) {
            if (s[0].equals("-0")) filenameSeparator = (char) 0;
            fileListFromStdin = true;
            String[] newArgs = new String[s.length - 1];
            System.arraycopy(s, 1, newArgs, 0, s.length - 1);
            s = newArgs;
        }
        if (s[0].equals("-v") || s[0].equals("--version")) {
            insist(s.length == 1);
            System.out.println("gcj-dbtool (" + System.getProperty("java.vm.name") + ") " + System.getProperty("java.vm.version"));
            System.out.println();
            System.out.println("Copyright 2006 Free Software Foundation, Inc.");
            System.out.println("This is free software; see the source for copying conditions.  There is NO");
            System.out.println("warranty; not even for MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.");
            return;
        }
        if (s[0].equals("--help")) {
            usage(System.out);
            return;
        }
        if (s[0].equals("-n")) {
            insist(s.length >= 2 && s.length <= 3);
            int capacity = 32749;
            if (s.length == 3) {
                capacity = Integer.parseInt(s[2]);
                if (capacity <= 2) {
                    usage(System.err);
                    System.exit(1);
                }
            }
            try {
                PersistentByteMap b = PersistentByteMap.emptyPersistentByteMap(new File(s[1]), capacity, capacity * 32);
            } catch (Exception e) {
                System.err.println("error: could not create " + s[1] + ": " + e.toString());
                System.exit(2);
            }
            return;
        }
        if (s[0].equals("-a") || s[0].equals("-f")) {
            try {
                insist(s.length == 4);
                File database = new File(s[1]);
                database = database.getAbsoluteFile();
                File jar = new File(s[2]);
                PersistentByteMap map;
                if (database.isFile()) map = new PersistentByteMap(database, PersistentByteMap.AccessMode.READ_ONLY); else map = PersistentByteMap.emptyPersistentByteMap(database, 100, 100 * 32);
                File soFile = new File(s[3]);
                if (!s[0].equals("-f") && !soFile.isFile()) throw new IllegalArgumentException(s[3] + " is not a file");
                map = addJar(jar, map, soFile);
            } catch (Exception e) {
                System.err.println("error: could not update " + s[1] + ": " + e.toString());
                System.exit(2);
            }
            return;
        }
        if (s[0].equals("-t")) {
            try {
                insist(s.length == 2);
                PersistentByteMap b = new PersistentByteMap(new File(s[1]), PersistentByteMap.AccessMode.READ_ONLY);
                Iterator iterator = b.iterator(PersistentByteMap.ENTRIES);
                while (iterator.hasNext()) {
                    PersistentByteMap.MapEntry entry = (PersistentByteMap.MapEntry) iterator.next();
                    byte[] key = (byte[]) entry.getKey();
                    byte[] value = (byte[]) b.get(key);
                    if (!Arrays.equals(value, (byte[]) entry.getValue())) {
                        String err = ("Key " + bytesToString(key) + " at bucket " + entry.getBucket());
                        throw new RuntimeException(err);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(3);
            }
            return;
        }
        if (s[0].equals("-m")) {
            insist(s.length >= 3 || fileListFromStdin && s.length == 2);
            try {
                File database = new File(s[1]);
                database = database.getAbsoluteFile();
                File temp = File.createTempFile(database.getName(), "", database.getParentFile());
                int newSize = 0;
                int newStringTableSize = 0;
                Fileset files = getFiles(s, 2, fileListFromStdin, filenameSeparator);
                PersistentByteMap[] sourceMaps = new PersistentByteMap[files.size()];
                {
                    Iterator it = files.iterator();
                    int i = 0;
                    while (it.hasNext()) {
                        PersistentByteMap b = new PersistentByteMap((File) it.next(), PersistentByteMap.AccessMode.READ_ONLY);
                        newSize += b.size();
                        newStringTableSize += b.stringTableSize();
                        sourceMaps[i++] = b;
                    }
                }
                newSize *= 1.5;
                PersistentByteMap map = PersistentByteMap.emptyPersistentByteMap(temp, newSize, newStringTableSize);
                for (int i = 0; i < sourceMaps.length; i++) {
                    if (verbose) System.err.println("adding " + sourceMaps[i].size() + " elements from " + sourceMaps[i].getFile());
                    map.putAll(sourceMaps[i]);
                }
                map.close();
                temp.renameTo(database);
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(3);
            }
            return;
        }
        if (s[0].equals("-l")) {
            insist(s.length == 2);
            try {
                PersistentByteMap b = new PersistentByteMap(new File(s[1]), PersistentByteMap.AccessMode.READ_ONLY);
                System.out.println("Capacity: " + b.capacity());
                System.out.println("Size: " + b.size());
                System.out.println();
                System.out.println("Elements: ");
                Iterator iterator = b.iterator(PersistentByteMap.ENTRIES);
                while (iterator.hasNext()) {
                    PersistentByteMap.MapEntry entry = (PersistentByteMap.MapEntry) iterator.next();
                    byte[] digest = (byte[]) entry.getKey();
                    System.out.print("[" + entry.getBucket() + "] " + bytesToString(digest) + " -> ");
                    System.out.println(new String((byte[]) entry.getValue()));
                }
            } catch (Exception e) {
                System.err.println("error: could not list " + s[1] + ": " + e.toString());
                System.exit(2);
            }
            return;
        }
        if (s[0].equals("-d")) {
            insist(s.length == 2);
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                PersistentByteMap b = new PersistentByteMap(new File(s[1]), PersistentByteMap.AccessMode.READ_WRITE);
                int N = b.capacity();
                byte[] bytes = new byte[1];
                byte digest[] = md.digest(bytes);
                for (int i = 0; i < N; i++) {
                    digest = md.digest(digest);
                    b.put(digest, digest);
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(3);
            }
            return;
        }
        if (s[0].equals("-p")) {
            insist(s.length == 1 || s.length == 2);
            String result;
            if (s.length == 1) result = System.getProperty("gnu.gcj.precompiled.db.path", ""); else result = (s[1] + (s[1].endsWith(File.separator) ? "" : File.separator) + getDbPathTail());
            System.out.println(result);
            return;
        }
        usage(System.err);
        System.exit(1);
    }
