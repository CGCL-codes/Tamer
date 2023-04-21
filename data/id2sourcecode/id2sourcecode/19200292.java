        public void onProgramme(TVProgramme programme) {
            if (!debugIncludeOldProgrammes && (programme.getStart() < (System.currentTimeMillis() - OLD_DATA))) {
                return;
            }
            File file = getFile(programme.getStart());
            TVData data = (TVData) filesData.get(file);
            if (data == null) {
                data = new TVData();
                filesData.put(file, data);
            }
            TVChannel ch = data.get(programme.getChannel().getID());
            ch.mergeHeaderFrom(programme.getChannel());
            ch.put(programme);
            StorageHelper.performInInfo(getInfo(), programme);
        }
