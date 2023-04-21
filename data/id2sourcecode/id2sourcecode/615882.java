    public void doBackup(File directory) throws Exception {
        if (!directory.exists()) directory.mkdir();
        RequestContext rc = RequestContext.getRequestContext();
        if (this.authStore != null) {
            Auth auth = this.authStore.retrieve(this.nsid);
            if (auth == null) this.authorize(); else rc.setAuth(auth);
        }
        PhotosetsInterface pi = flickr.getPhotosetsInterface();
        PhotosInterface photoInt = flickr.getPhotosInterface();
        Map allPhotos = new HashMap();
        Iterator sets = pi.getList(this.nsid).getPhotosets().iterator();
        while (sets.hasNext()) {
            Photoset set = (Photoset) sets.next();
            PhotoList photos = pi.getPhotos(set.getId(), 500, 1);
            allPhotos.put(set.getTitle(), photos);
        }
        int notInSetPage = 1;
        Collection notInASet = new ArrayList();
        while (true) {
            Collection nis = photoInt.getNotInSet(50, notInSetPage);
            notInASet.addAll(nis);
            if (nis.size() < 50) break;
            notInSetPage++;
        }
        allPhotos.put("NotInASet", notInASet);
        Iterator allIter = allPhotos.keySet().iterator();
        while (allIter.hasNext()) {
            String setTitle = (String) allIter.next();
            String setDirectoryName = makeSafeFilename(setTitle);
            Collection currentSet = (Collection) allPhotos.get(setTitle);
            Iterator setIterator = currentSet.iterator();
            File setDirectory = new File(directory, setDirectoryName);
            setDirectory.mkdir();
            while (setIterator.hasNext()) {
                Photo p = (Photo) setIterator.next();
                String url = p.getLargeUrl();
                URL u = new URL(url);
                String filename = u.getFile();
                filename = filename.substring(filename.lastIndexOf("/") + 1, filename.length());
                System.out.println("Now writing " + filename + " to " + setDirectory.getCanonicalPath());
                BufferedInputStream inStream = new BufferedInputStream(photoInt.getImageAsStream(p, Size.LARGE));
                File newFile = new File(setDirectory, filename);
                FileOutputStream fos = new FileOutputStream(newFile);
                int read;
                while ((read = inStream.read()) != -1) {
                    fos.write(read);
                }
                fos.flush();
                fos.close();
                inStream.close();
            }
        }
    }
