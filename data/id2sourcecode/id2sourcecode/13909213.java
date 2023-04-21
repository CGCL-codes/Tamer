    private void rollover(File fromFile, int fromIndex, int toIndex) throws IOException {
        if (toIndex > MAX_HISTORY || !fromFile.exists()) {
            return;
        }
        File toFile = new File(fromFile + "." + toIndex);
        if (toFile.exists()) {
            rollover(fromFile, toIndex, toIndex + 1);
        }
        TextUtils.writeTextFile(toFile.getAbsolutePath(), TextUtils.readTextFile(new File(fromFile.getAbsoluteFile() + (fromIndex > 0 ? "." + fromIndex : ""))));
    }
