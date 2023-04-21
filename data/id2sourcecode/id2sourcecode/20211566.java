    public void setSubPages(Collection<IWikiPage> subpages) {
        if ((subpages == null) || (subpages.size() == 0)) return;
        for (IWikiPage page : subpages) {
            String s = page.getName();
            if (s.startsWith("attachment:")) {
                s = s.replaceFirst("attachment:", "");
                String name = s.substring(0, s.indexOf('/'));
                s = s.replaceFirst(name + "/parentname:", "");
                String parent = s.substring(0, s.indexOf('/'));
                s = s.replaceFirst(parent + "/", "");
                File attachStorageDir = new File(((WikiRepositoryFS) this.repository).getStorageDir().getAbsolutePath() + "\\" + parent + "." + "attachments");
                attachStorageDir.mkdirs();
                File file = new File(attachStorageDir, name);
                FileWriter writer = Util.getFileWriter(file);
                InputStream in = null;
                Reader reader = null;
                if (s.startsWith("file:///")) {
                    reader = Util.getAsReader(new File(s.replaceFirst("file:///", "")));
                } else {
                    in = Util.getInputStreamFromUrl(s);
                    reader = new BufferedReader(new InputStreamReader(in));
                }
                Util.copy(reader, writer);
            }
        }
    }
