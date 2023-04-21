    private static String importFromMedia(String location, String destination, Site site, Date start, Date end, LoadingBar lb) throws IOException {
        String log = "";
        File folder = new File(location);
        File[] listOfFiles = folder.listFiles(new FileFilter() {

            @Override
            public boolean accept(File file) {
                boolean isJPG = file.getName().toLowerCase().endsWith(".jpg");
                boolean isDir = file.isDirectory();
                return isJPG || isDir;
            }
        });
        String[] fileNames = new String[listOfFiles.length];
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                String loc = String.format("%s/%s", folder.getCanonicalPath(), listOfFiles[i].getName());
                String oldFileName = listOfFiles[i].getName();
                String ext = oldFileName.substring(oldFileName.lastIndexOf("."));
                fileNames[i] = listOfFiles[i].getName();
                java.util.Date d = DateFromExif(listOfFiles[i]);
                Debug.print(d);
                if (d.after(start) && d.before(end)) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
                    String fileName = String.format("%s_%s%s", site.getSite(), sdf.format(d), ext);
                    String dest = String.format("%s/%s", destination, fileName);
                    File dfile = new File(dest);
                    if (!dfile.exists()) {
                        String txt = "Copying " + fileNames[i] + " " + fileName;
                        lb.increment(txt);
                        log += "Added and renamed \"" + fileNames[i] + "\" as " + fileName + "\n";
                        NestImage ni = site.addImage(d, fileName);
                        ni.setTemplate(site.getMainTemplate());
                        if (ni.getTemplate() != null) log += " -- Applied template (" + ni.getTemplate().toString() + ")\n";
                        for (int j = 0; j < site.getCountNames().size(); j++) {
                            ni.setCountAsCounted(j, false);
                        }
                        copyFile(new File(loc), dfile);
                    } else {
                        String txt = "Skipped " + fileNames[i] + " file exists as " + fileName;
                        lb.increment(txt);
                        log += txt + "\n";
                    }
                } else {
                    String txt = "Skipped " + fileNames[i] + " - out of date range. (" + Conversions.getFullDate().format(d) + ")";
                    lb.increment(txt);
                    log += txt + "\n";
                }
            } else {
                String txt = "Skipped " + fileNames[i] + " - not a file..";
                lb.increment(txt);
                log += txt + "\n";
            }
            if (listOfFiles[i].isDirectory()) {
                String locationTemp = folder.toString() + "/" + listOfFiles[i].getName();
                Debug.print("" + locationTemp + " " + destination);
                log += importFromMedia(locationTemp, destination, site, start, end, lb);
            }
        }
        return log;
    }
