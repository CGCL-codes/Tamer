    public void addFile(String name, String type) {
        int currentPosition = -1;
        for (int i = 0; i < MAX_FILES; i++) {
            if (files[i] != null && files[i].equals(name)) {
                currentPosition = i;
            }
        }
        if (currentPosition == 0) {
            return;
        }
        if (currentPosition > 0) {
            for (int i = currentPosition; i < MAX_FILES - 1; i++) {
                files[i] = files[i + 1];
                fileTypes[i + 1] = fileTypes[i];
            }
        }
        for (int j = MAX_FILES - 2; j >= 0; j--) {
            files[j + 1] = files[j];
            fileTypes[j + 1] = fileTypes[j];
        }
        files[0] = name;
        fileTypes[0] = type;
        fileList.setListData(files);
        fileList.setSelectedIndex(0);
        pack();
        saveList();
    }
