    private void initRawPackageInfo(HashtableOfArrayToObject rawPackageInfo, String entryName, boolean isDirectory, String sourceLevel, String compliance) {
        int lastSeparator = isDirectory ? entryName.length() - 1 : entryName.lastIndexOf('/');
        String[] pkgName = Util.splitOn('/', entryName, 0, lastSeparator);
        String[] existing = null;
        int length = pkgName.length;
        int existingLength = length;
        while (existingLength >= 0) {
            existing = (String[]) rawPackageInfo.getKey(pkgName, existingLength);
            if (existing != null) break;
            existingLength--;
        }
        JavaModelManager manager = JavaModelManager.getJavaModelManager();
        for (int i = existingLength; i < length; i++) {
            if (Util.isValidFolderNameForPackage(pkgName[i], sourceLevel, compliance)) {
                System.arraycopy(existing, 0, existing = new String[i + 1], 0, i);
                existing[i] = manager.intern(pkgName[i]);
                rawPackageInfo.put(existing, new ArrayList[] { EMPTY_LIST, EMPTY_LIST });
            } else {
                if (!isDirectory) {
                    ArrayList[] children = (ArrayList[]) rawPackageInfo.get(existing);
                    if (children[1] == EMPTY_LIST) children[1] = new ArrayList();
                    children[1].add(entryName);
                }
                return;
            }
        }
        if (isDirectory) return;
        ArrayList[] children = (ArrayList[]) rawPackageInfo.get(pkgName);
        if (org.eclipse.jdt.internal.compiler.util.Util.isClassFileName(entryName)) {
            if (children[0] == EMPTY_LIST) children[0] = new ArrayList();
            String nameWithoutExtension = entryName.substring(lastSeparator + 1, entryName.length() - 6);
            children[0].add(nameWithoutExtension);
        } else {
            if (children[1] == EMPTY_LIST) children[1] = new ArrayList();
            children[1].add(entryName);
        }
    }
