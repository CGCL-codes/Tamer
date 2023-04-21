    public String execute() {
        try {
            String filePath = servletRequest.getRealPath("/whiteboard/upload");
            System.out.println("Server Path :" + filePath);
            System.out.println("FILE NAME: " + userImageFileName);
            File fileToCreate = new File(filePath, this.userImageFileName);
            this.userImageFileName = "upload/" + this.userImageFileName;
            FileUtils.copyFile(this.userImage, fileToCreate);
            HomePage homepage = getHomePage();
            Course selectedCourse = homepage.getSelectedCourse();
            String action = selectedCourse.getAction();
            String typeOfDoc = DocumentAction.findTypeOfDoc(action);
            String typeOfAction = DocumentAction.findTypeOfAction(action);
            String whatDocument = selectedCourse.getWhatDocument();
            if (typeOfAction.compareTo("edit") == 0) {
                Document selectedDoc = null;
                if (typeOfDoc.compareTo("notes") == 0) {
                    selectedDoc = selectedCourse.extractNote(whatDocument);
                } else if (typeOfDoc.compareTo("announcements") == 0) {
                    selectedDoc = selectedCourse.extractAnnouncement(whatDocument);
                } else if (typeOfDoc.compareTo("homework") == 0) {
                    selectedDoc = selectedCourse.extractHomework(whatDocument);
                } else {
                    System.out.println("ERRor in upload");
                }
                selectedDoc.setLink(userImageFileName);
            } else {
                selectedCourse.setTempLink(userImageFileName);
                if (getText() == null) {
                    selectedCourse.setTempText("");
                } else {
                    selectedCourse.setTempText(getText());
                }
                if (getTitle() == null) {
                    selectedCourse.setTempTitle("");
                } else {
                    selectedCourse.setTempTitle(getTitle());
                }
                System.out.println("Set temp text and title to:---->" + getText() + "   " + getTitle());
                System.out.println("Set temp link to " + userImageFileName);
                String text2 = (String) servletContext.getAttribute("text");
                System.out.println("from  context :" + text2);
            }
        } catch (Exception e) {
            return INPUT;
        }
        return SUCCESS;
    }
