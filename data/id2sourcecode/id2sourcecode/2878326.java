    public static void storeSubpage(File rootRepoDirectory, String s) {
        if (s.startsWith("attachment:")) {
            s = s.replaceFirst("attachment:", "");
            String name = s.substring(0, s.indexOf('/'));
            s = s.replaceFirst(name + "/parentname:", "");
            String parent = s.substring(0, s.indexOf('/'));
            s = s.replaceFirst(parent + "/", "");
            File attachStorageDir = new File(rootRepoDirectory.getAbsolutePath() + "\\" + parent + "." + "attachments");
            attachStorageDir.mkdirs();
            File file = new File(attachStorageDir, name);
            FileWriter writer = getFileWriter(file);
            InputStream in = null;
            Reader reader = null;
            if (s.startsWith("file:///")) {
                reader = getAsReader(new File(s.replaceFirst("file:///", "")));
            } else {
                in = getInputStreamFromUrl(s);
                reader = new BufferedReader(new InputStreamReader(in));
            }
            copy(reader, writer);
        }
    }
