    public void changeFolder(FolderItemListLayout folder, String tag) {
        List<AICFile> files = new ArrayList<AICFile>();
        if (tag.startsWith("@")) {
            List<JsonObject> file2 = new ArrayList<JsonObject>();
            try {
                file2.addAll(CloudFileManagementService.get_contents_by_tag(model.getSessionId(), tag));
            } catch (Exception e) {
                e.printStackTrace();
            }
            for (int i = 0; i < file2.size(); i++) {
                AICFile file = new AICFile();
                JsonObject jfile = file2.get(i);
                file.setName(jfile.getValue("name").toString());
                file.setKey(jfile.getValue("key").toString());
                file.setContent_type(jfile.getValue("content_type").toString());
                file.setLast_modified(jfile.getValue("last_modified").toString());
                file.setIs_shared(jfile.getValue("is_shared").toString());
                try {
                    file.setFile_type(jfile.getValue("file_type").toString());
                } catch (NullPointerException e) {
                    Log.d("CLOUD_DEBUG", "No_type");
                }
                Log.d("CLOUD_DEBUG_jfile", jfile.toString());
                files.add(file);
            }
        } else if (tag.startsWith("#")) {
            String ttag = tag.substring(1);
            List<JsonObject> file2 = new ArrayList<JsonObject>();
            String filePath = "/" + ttag;
            try {
                file2.addAll(LocalPhoneFileManagementService.get_contents_by_tag_local(filePath));
            } catch (Exception e) {
                e.printStackTrace();
            }
            for (int i = 0; i < file2.size(); i++) {
                AICFile file = new AICFile();
                JsonObject jfile = file2.get(i);
                file.setName(jfile.getValue("name").toString());
                file.setKey("wei jiang key");
                file.setContent_type(jfile.getValue("content_type").toString());
                Log.d("content_type", jfile.getValue("content_type").toString());
                file.setLast_modified("wei jiang last modified");
                file.setIs_shared("wei jiang is shared");
                Log.d("PHONE_DEBUG", jfile.toString());
                files.add(file);
            }
        }
        folder.getModel().setFiles(files);
        folder.getModel().setFolderTag(tag);
        String[] recent = model.getRecent();
        boolean full = true;
        for (int i = 0; i < recent.length; i++) {
            if (recent[i].equals(tag)) {
                full = false;
                break;
            }
            if (recent[i].equals("")) {
                recent[i] = tag;
                full = false;
                break;
            }
        }
        if (full) {
            for (int i = 0; i < recent.length - 1; i++) {
                recent[i] = recent[i + 1];
            }
            recent[recent.length - 1] = tag;
        }
        activity.refresh();
        folder.refresh();
    }
