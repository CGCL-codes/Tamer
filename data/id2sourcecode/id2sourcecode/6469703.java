    public void run() {
        this.lastError = null;
        try {
            String photosetID = null;
            String newset_name = null;
            boolean publicFlag, familyFlag, friendFlag;
            int idx = this.privacyChoice.getSelectedIndex();
            switch(idx) {
                case 1:
                    publicFlag = familyFlag = false;
                    friendFlag = true;
                    break;
                case 2:
                    publicFlag = friendFlag = false;
                    familyFlag = true;
                    break;
                case 3:
                    publicFlag = false;
                    familyFlag = friendFlag = true;
                    break;
                case 4:
                    publicFlag = familyFlag = friendFlag = false;
                    break;
                default:
                    publicFlag = familyFlag = friendFlag = true;
                    break;
            }
            idx = this.photosetChoice.getSelectedIndex();
            if (idx > 1) {
                idx--;
                for (Iterator it = this.currentPhotosets.iterator(); idx > 0 && it.hasNext(); ) {
                    Object element = it.next();
                    idx--;
                    if (idx == 0) photosetID = ((Photoset) element).getId();
                }
            } else if (idx == 1) {
                newset_name = this.newsetField.getText().trim();
                if (newset_name.length() > 0) {
                    photosetID = "CREATEME";
                }
            }
            String url_suffix = "";
            switch(this.resizeChoice.getSelectedIndex()) {
                case 1:
                    url_suffix = "?size=640";
                    break;
                case 2:
                    url_suffix = "?size=800";
                    break;
                case 3:
                    url_suffix = "?size=1024";
                    break;
                case 4:
                    url_suffix = "?size=1200";
                    break;
                case 5:
                    url_suffix = "?size=1600";
                    break;
                default:
                    url_suffix = "?size=0";
            }
            List tags = new ArrayList();
            Pattern pattern = Pattern.compile("(?:([^\\s\"]\\S*)|(?:\"((?:i|[^i])*?)(?:(?:\"\\s)|(?:\"$)|$)))");
            Matcher matcher = pattern.matcher(this.tagsField.getText());
            while (matcher.find()) {
                tags.add('"' + matcher.group(matcher.group(1) == null ? 2 : 1) + '"');
            }
            int md5Idx = tags.size();
            int fnIdx = md5Idx;
            byte[] buf = new byte[1024];
            MessageFormat messageForm = new MessageFormat("Uploading photo #{0} of {1} {6,choice,0#|0<({6} skipped)}... {2}% {3,choice,0#|0<(eta: {4} min {5} sec)}");
            Object[] messageArgs = { new Integer(1), new Integer(this.nbPhotos), new Integer(0), new Long(0), new Integer(0), new Integer(0), new Integer(0) };
            boolean computeMD5 = this.md5Check.getState();
            boolean existMD5 = this.existCheck.getState();
            boolean saveFilename = this.filenameCheck.getState();
            if (computeMD5) {
                tags.add("picasa2flickr:md5=none");
                fnIdx++;
            }
            if (saveFilename) tags.add("picasa2flickr:filename=none");
            long eta = 0;
            long uploadTotal = 0;
            long uploadCurrent = 0;
            if (this.resizeChoice.getSelectedIndex() == 0) {
                for (int i = 0; i < this.nbPhotos; i++) {
                    URL photoURL = new URL(this.photoURL[i].toString() + url_suffix);
                    URLConnection urlConn = photoURL.openConnection();
                    urlConn.setDoInput(true);
                    urlConn.setUseCaches(false);
                    DataInputStream dis = new DataInputStream(urlConn.getInputStream());
                    uploadTotal += urlConn.getContentLength();
                    dis.close();
                }
            }
            long startTime = System.currentTimeMillis();
            int skipedUpload = 0;
            for (int i = 0; i < this.nbPhotos; i++) {
                URL photoURL = new URL(this.photoURL[i].toString() + url_suffix);
                URLConnection urlConn = photoURL.openConnection();
                boolean skip = false;
                urlConn.setDoInput(true);
                urlConn.setUseCaches(false);
                DataInputStream dis = new DataInputStream(urlConn.getInputStream());
                if (computeMD5) {
                    MessageDigest hash = MessageDigest.getInstance("MD5");
                    int nbRead = 0;
                    while ((nbRead = dis.read(buf)) != -1) {
                        hash.update(buf, 0, nbRead);
                    }
                    dis.close();
                    String md5Str = "picasa2flickr:md5=" + toHexString(hash.digest(), 0, 0);
                    tags.set(md5Idx, md5Str);
                    urlConn = photoURL.openConnection();
                    urlConn.setDoInput(true);
                    urlConn.setUseCaches(false);
                    dis = new DataInputStream(urlConn.getInputStream());
                    if (existMD5) {
                        if (this.uploader.isPhotoExist(md5Str, this.currentUser)) {
                            System.out.println("Photo " + this.photoTitles[i] + " skipped");
                            skip = true;
                            skipedUpload += 1;
                        }
                    }
                }
                if (saveFilename) {
                    tags.set(fnIdx, "picasa2flickr:filename=\"" + this.photoTitles[i] + "\"");
                }
                messageArgs[0] = new Integer(i + 1);
                messageArgs[2] = new Integer((int) (0.5 + 100. * (float) i / this.nbPhotos));
                messageArgs[3] = new Long(eta);
                messageArgs[4] = new Integer((int) (eta / 60));
                messageArgs[5] = new Integer((int) (eta % 60));
                messageArgs[6] = new Integer(skipedUpload);
                this.infoLabel.setText(messageForm.format(messageArgs));
                this.infoLabel.repaint();
                Thread.yield();
                String photoID = null;
                if (!skip) {
                    uploadCurrent += urlConn.getContentLength();
                    if (this.removeExtCheck.getState() && this.photoTitles[i].lastIndexOf('.') != -1) {
                        this.photoTitles[i] = this.photoTitles[i].substring(0, this.photoTitles[i].lastIndexOf('.'));
                    }
                    photoID = this.uploader.uploadPhoto(this.currentUser, dis, this.photoTitles[i], this.photoDesc[i], tags, publicFlag, familyFlag, friendFlag);
                } else {
                    uploadTotal -= urlConn.getContentLength();
                }
                long duration = System.currentTimeMillis() - startTime;
                if (uploadTotal > 0 && uploadCurrent > 0) {
                    eta = ((uploadTotal - uploadCurrent) * duration) / (1000 * uploadCurrent);
                }
                dis.close();
                if (photoID != null && photosetID != null) {
                    if (photosetID == "CREATEME") {
                        photosetID = null;
                        try {
                            photosetID = this.uploader.createPhotoset(this.currentUser, newset_name, photoID);
                        } catch (Exception e) {
                            Collection sets = this.uploader.retrivePhotosetsList(this.currentUser);
                            if (!sets.isEmpty()) {
                                Photoset pset = (Photoset) (sets.iterator().next());
                                if (pset.getTitle().equalsIgnoreCase(newset_name)) {
                                    photosetID = pset.getId();
                                }
                            }
                        }
                    } else {
                        this.uploader.addPhotoToPhotoset(this.currentUser, photoID, photosetID);
                    }
                }
            }
            this.infoLabel.setText((this.nbPhotos - skipedUpload) + " photo(s) uploaded (" + skipedUpload + " skipped). You can close the window using the link below.");
            this.nbPhotos = 0;
        } catch (Exception e) {
            this.lastError = e.getLocalizedMessage();
            e.printStackTrace();
        }
        this.uploading = false;
        this.updateStatus();
    }
