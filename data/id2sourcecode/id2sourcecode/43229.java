    private static int checkExisting(ProjectInclude include) {
        File file = new File(include.path);
        if (file.exists()) {
            Date fileModified = new Date(file.lastModified());
            int dateResult = fileModified.compareTo(include.modified);
            if (dateResult > 0) {
                int result = JOptionPane.showConfirmDialog(null, "The file " + include.path + " already exists in the source directory," + " and is newer than the one in the imported project. OK to " + "overwrite the newer file?\n\n" + "Source directory: " + fileModified.toString() + " (newer)\n" + "Project: " + include.modified.toString());
                if (result == JOptionPane.YES_OPTION) return 1; else if (result == JOptionPane.CANCEL_OPTION) return -1;
                return 0;
            } else if (dateResult < 0) {
                int result = JOptionPane.showConfirmDialog(null, "The file " + include.path + " already exists in the source directory," + " and is older than the one in the imported project. OK to " + "overwrite the older file?\n\n" + "Source directory: " + fileModified.toString() + "\n" + "Project: " + include.modified.toString() + " (newer)");
                if (result == JOptionPane.YES_OPTION) return 1; else if (result == JOptionPane.CANCEL_OPTION) return -1;
                return 0;
            }
        }
        return 1;
    }
