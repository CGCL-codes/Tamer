    protected void recursively(File f) throws IOException {
        if (f.isDirectory()) {
            if (excludeDirs.contains(f.getName())) return;
            for (File kid : f.listFiles()) recursively(kid);
        } else {
            boolean skip = true;
            String name = f.getName();
            for (String ext : validExtenstions) if (name.endsWith(ext)) {
                skip = false;
                break;
            }
            if (skip) return;
            System.out.println("handling file: " + f.getAbsolutePath());
            File newFile = handleFile(f.getAbsolutePath());
            copyFile(newFile, f);
            System.out.println("file " + f.getAbsolutePath() + " " + "was replaced by " + newFile.getAbsolutePath());
        }
    }
