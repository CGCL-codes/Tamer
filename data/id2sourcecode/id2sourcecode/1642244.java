    public void copyD5InD4(File src) throws IOException {
        if (src.isDirectory()) {
            File listdir[] = src.listFiles();
            for (int i = 0; i < listdir.length; i++) {
                if (this.blackList.contains(listdir[i].getName())) {
                    continue;
                }
                this.copyD5InD4(new File(src + "/" + listdir[i].getName()));
            }
        } else {
            String relativepath = src.toString().substring(srcRoot.length());
            File copydest = new File(destRoot + "client/" + relativepath.substring(0, relativepath.length() - src.getName().length()));
            File modfile = new File(destRoot + "client/" + relativepath.substring(0, relativepath.length()));
            Pattern p = Pattern.compile("[a-zA-Z0-9]+.java");
            Matcher m = p.matcher(src.getName());
            boolean b = m.matches();
            if (b) {
                createNewFile(src, modfile);
                System.out.println(relativepath.substring(0, relativepath.length()));
            } else {
                FileUtils.copyFileToDirectory(src, copydest);
            }
        }
    }
