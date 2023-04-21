    public void mergeCss() throws IOException {
        List csslist = new ArrayList();
        csslist.add("base/Aurora-all.css");
        csslist.add("base/Aurora-all-min.css");
        csslist.add("grid/Grid.css");
        csslist.add("grid/Grid-min.css");
        csslist.add("tab/Tab.css");
        csslist.add("tab/Tab-min.css");
        StringBuffer sb = new StringBuffer();
        List patchlines = new ArrayList();
        Iterator it1 = csslist.iterator();
        File current = new File(".");
        while (it1.hasNext()) {
            String dest = (String) it1.next();
            File file = new File(current, RES_DIR + dest);
            File destFile = new File(current, THEME_DARBLUE_DIR + dest);
            FileUtils.copyFile(file, destFile);
        }
        Iterator it = csslist.iterator();
        ;
        while (it.hasNext()) {
            String dest = (String) it.next();
            File file = new File(current, THEME_DARBLUE_DIR + dest);
            File patch = new File(file.getParentFile(), "patch.css");
            if (patch.exists()) {
                List ls = FileUtils.readLines(patch, "UTF-8");
                for (int i = 0; i < ls.size(); i++) {
                    String line = ls.get(i).toString();
                    patchlines.add(line);
                }
            }
            List lines = new ArrayList();
            List fs = FileUtils.readLines(file, "UTF-8");
            for (int i = 0; i < fs.size(); i++) {
                String line = fs.get(i).toString();
                lines.add(line);
            }
            lines.addAll(patchlines);
            FileUtils.writeLines(new File(current, THEME_DARBLUE_DIR + dest), "UTF-8", lines);
        }
    }
