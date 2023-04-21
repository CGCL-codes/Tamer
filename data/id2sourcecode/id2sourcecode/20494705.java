    public ArrayList<FileDescriptorMD5> getListofFilesThatHasDifferentMD5tobeDownloaded(ArrayList<FileDescriptorMD5> listofFilesfromServer, String path) {
        try {
            getFilesFromDirectory(path);
            if (listOfFiles.size() > 0) {
                for (FileDescriptorMD5 filefromServer : listofFilesfromServer) {
                    boolean filenotexistintheclient = true;
                    for (File fileInTheClient : listOfFiles) {
                        if (fileInTheClient.getName().equals(filefromServer.getName())) {
                            MessageDigest md = MessageDigest.getInstance("MD5");
                            BigInteger hash = new BigInteger(1, md.digest(fileInTheClient.toString().getBytes()));
                            if (!filefromServer.getMD5().equals(hash.toString(16))) {
                                filenotexistintheclient = false;
                                listOfFilesThatNeedToBeDownloaded.add(filefromServer);
                            }
                        }
                    }
                    if (filenotexistintheclient) {
                        listOfFilesThatNeedToBeDownloaded.add(filefromServer);
                    }
                }
            } else {
                listOfFilesThatNeedToBeDownloaded.addAll(listofFilesfromServer);
            }
            for (FileDescriptorMD5 filechec : listOfFilesThatNeedToBeDownloaded) {
                System.out.println(filechec.getLocation() + " " + filechec.getName());
            }
            return listOfFilesThatNeedToBeDownloaded;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
