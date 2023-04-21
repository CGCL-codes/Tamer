    public Object execute(ExecutionEvent event) throws ExecutionException {
        System.err.println(" Expanding sedx archive...");
        IStructuredSelection iss = (IStructuredSelection) HandlerUtil.getCurrentSelection(event);
        if (iss == null) {
            return null;
        }
        IFile selected = (IFile) iss.getFirstElement();
        try {
            byte[] buf = new byte[1024];
            System.err.println(selected.getLocation().toOSString());
            ZipFile zipFile = new ZipFile(selected.getLocation().toOSString());
            Enumeration entries = zipFile.entries();
            InputStream zis = null;
            while (entries.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                {
                    String entryName = entry.getName();
                    System.out.println("entryname " + entryName);
                    int n;
                    FileOutputStream fileoutputstream;
                    File newFile = new File(entryName);
                    String directory = newFile.getParent();
                    if (directory == null) {
                        if (newFile.isDirectory()) break;
                    }
                    fileoutputstream = new FileOutputStream(selected.getParent().getLocation().toOSString() + File.separator + entryName);
                    zis = zipFile.getInputStream(entry);
                    while ((n = zis.read(buf, 0, 1024)) > -1) fileoutputstream.write(buf, 0, n);
                    fileoutputstream.close();
                    zis.close();
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            selected.getParent().refreshLocal(IResource.DEPTH_ONE, null);
        } catch (CoreException e) {
            e.printStackTrace();
        }
        return null;
    }
