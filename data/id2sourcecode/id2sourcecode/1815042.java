    private void browserDirectory(File directory) {
        if (!directory.isDirectory()) return;
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) browserDirectory(file); else if (file.isFile() && MediaFile.isMediaFile(file)) {
                    try {
                        MediaFile newMediaFile = MediaFile.create(file);
                        if (!mediaFiles.containsKey(newMediaFile.getPath())) {
                            sqlServer.insertMediaFile(newMediaFile);
                            nbNewMediaFiles++;
                        } else {
                            MediaFile oldMediaFile = mediaFiles.get(newMediaFile.getPath());
                            if (oldMediaFile.compareTo(newMediaFile) != 0) {
                                System.out.println(oldMediaFile);
                                sqlServer.deleteMediaFile(oldMediaFile);
                                sqlServer.insertMediaFile(newMediaFile);
                                nbUpdatedMediaFiles++;
                            }
                            mediaFiles.remove(newMediaFile.getPath());
                        }
                    } catch (SQLException sqle) {
                        sqle.printStackTrace();
                    }
                }
            }
        }
    }
