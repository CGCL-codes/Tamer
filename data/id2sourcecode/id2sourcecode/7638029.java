        protected void onProgramme(TVProgramme programme) {
            final String fileName = getFileName(programme.getStart());
            if (fileName == null) {
                return;
            }
            TVData data = (TVData) filesData.get(fileName);
            if (data == null) {
                data = new TVData();
                filesData.put(fileName, data);
            }
            TVChannel ch = data.get(programme.getChannel().getID());
            ch.put(programme);
        }
