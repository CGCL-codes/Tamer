    public static void removeFiles(String path, String listFileName) {
        try {
            FileReader fr = new FileReader(listFileName);
            BufferedReader br = new BufferedReader(fr);
            HashSet<String> fileList = new HashSet<String>();
            String fileName = br.readLine();
            while (fileName != null) {
                fileList.add(fileName);
                fileName = br.readLine();
            }
            int nFileRemoved = 0;
            int nFileTotal = 0;
            File dir = new File(path);
            for (File file : dir.listFiles()) {
                nFileTotal++;
                if (fileList.contains(file.getName())) {
                    file.delete();
                    nFileRemoved++;
                }
            }
            int nFileRemain = nFileTotal - nFileRemoved;
            System.out.println("the total number of files: " + nFileTotal);
            System.out.println("the number of files removed: " + nFileRemoved);
            System.out.println("the number of files remained: " + nFileRemain);
            br.close();
            fr.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
