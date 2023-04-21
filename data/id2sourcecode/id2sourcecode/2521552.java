    public void checkFileExist() {
        int len = 0;
        String checkFilename = fileName + ".asc";
        int i = 0;
        File checkfile[];
        File dir = new File(filePath);
        if (dir.isDirectory() == true) {
            checkfile = dir.listFiles();
            len = checkfile.length;
            String tmpname = "";
            for (i = 0; i < len; i++) {
                tmpname = checkfile[i].getName().toString();
                if (checkFilename.equals(tmpname)) {
                    fileExistErr = 1;
                    if (checkfile[i].delete()) {
                        System.out.println(tmpname + " deleted");
                    }
                    break;
                }
            }
        } else {
            directoryErr = 1;
        }
    }
