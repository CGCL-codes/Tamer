    public ActionForward process(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        FileForm ff = (FileForm) form;
        int fileId = ff.getId();
        ff.setRequest(request);
        File currFile = new File(fileId);
        ff.fillBean(currFile);
        currFile.store();
        ff.setId(currFile.getId());
        String uploadDir = getServlet().getServletContext().getInitParameter("uploadedFilePath");
        HashMap deleteFile = ff.getAllDeleteFile();
        for (Iterator i = deleteFile.entrySet().iterator(); i.hasNext(); ) {
            Map.Entry entry = (Map.Entry) i.next();
            String[] type_format = ((String) entry.getKey()).split("_");
            int typeId = Integer.parseInt(type_format[0]);
            int formatId = Integer.parseInt(type_format[1]);
            if (entry.getValue().equals("on")) {
                System.out.println(">>>>>>>>>> rimuovo formato " + formatId);
                currFile.removeFormato(formatId, uploadDir);
            }
        }
        HashMap uFile = ff.getAllFiles();
        for (Iterator i = uFile.entrySet().iterator(); i.hasNext(); ) {
            Map.Entry entry = (Map.Entry) i.next();
            String[] type_format = ((String) entry.getKey()).split("_");
            int typeId = Integer.parseInt(type_format[0]);
            int formatId = Integer.parseInt(type_format[1]);
            if (typeId == currFile.getIdType()) if (((FormFile) entry.getValue()).getFileName().length() > 0) {
                currFile.addFormat(formatId);
                FileFormat currFileFormat = new FileFormat(formatId);
                String targetFileName = currFile.getId() + currFileFormat.getExt();
                InputStream uis = ((FormFile) entry.getValue()).getInputStream();
                FileOutputStream fos = new FileOutputStream(uploadDir + targetFileName);
                int c;
                while ((c = uis.read()) != -1) fos.write(c);
                uis.close();
                fos.close();
            }
        }
        String[] catIdArray = ff.getIdCat();
        currFile.removeAllCategories();
        for (int i = 0; i < catIdArray.length; i++) {
            int currIntCatId = Integer.parseInt(catIdArray[i]);
            currFile.addCategory(currIntCatId);
        }
        ActionForward frw = mapping.findForward("success");
        ActionForward frw1 = new ActionForward(frw.getPath() + "?id=" + currFile.getId(), true);
        return frw1;
    }
