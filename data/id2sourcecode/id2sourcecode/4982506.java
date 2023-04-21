    public ActionForward getDataFromFile(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse response) {
        ActionMessages errors = new ActionMessages();
        boolean isWindows = (System.getProperty("os.name").indexOf("Win") != -1) ? true : false;
        String tmpDirectory = ((isWindows) ? mProp.getProperty("bzip2.outputFilePath.windows") : mProp.getProperty("bzip2.outputFilePath"));
        try {
            Integer proposalId = (Integer) request.getSession().getAttribute(Constants.PROPOSAL_ID);
            Integer imageId = new Integer(request.getParameter(Constants.IMAGE_ID));
            ImageFacadeLocal imgFacade = ImageFacadeUtil.getLocalHome().create();
            ArrayList imageFetchedList = (ArrayList) imgFacade.findByImageIdAndProposalId(imageId, proposalId);
            if (imageFetchedList.size() == 1) {
                ImageLightValue selectedImage = ((ImageLightValue) imageFetchedList.get(0));
                String _sourceFileName = selectedImage.getFileLocation() + "/" + selectedImage.getFileName();
                _sourceFileName = (isWindows) ? "C:" + _sourceFileName : _sourceFileName;
                String _destinationFileName = selectedImage.getFileName();
                String _destinationfullFilename = tmpDirectory + "/" + _destinationFileName;
                String _bz2FileName = _destinationFileName + ".bz2";
                String _bz2FullFileName = _destinationfullFilename + ".bz2";
                File source = new File(_sourceFileName);
                File destination = new File(_destinationfullFilename);
                File bz2File = new File(_bz2FullFileName);
                FileUtils.copyFile(source, destination, false);
                String cmd = ((isWindows) ? mProp.getProperty("bzip2.path.windows") : mProp.getProperty("bzip2.path"));
                String argument = mProp.getProperty("bzip2.arguments");
                argument = " " + argument + " " + _destinationfullFilename;
                cmd = cmd + argument;
                this.CmdExec(cmd, false);
                Date now = new Date();
                long startTime = now.getTime();
                long timeNow = now.getTime();
                long timeOut = 60000;
                boolean filePresent = false;
                while (!filePresent && (timeNow - startTime) < timeOut) {
                    Date d2 = new Date();
                    timeNow = d2.getTime();
                    filePresent = bz2File.exists();
                }
                if (filePresent) Thread.sleep(10000);
                byte[] imageBytes = FileUtil.readBytes(_bz2FullFileName);
                response.setContentLength(imageBytes.length);
                response.setHeader("Content-Disposition", "attachment; filename=" + _bz2FileName);
                response.setContentType("application/x-bzip");
                ServletOutputStream out = response.getOutputStream();
                out.write(imageBytes);
                out.flush();
                out.close();
                FileCleaner fileCleaner = new FileCleaner(60000, _bz2FullFileName);
                fileCleaner.start();
                return null;
            } else {
                errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.user.results.viewImageId", imageId));
                ClientLogger.getInstance().warn("List fetched has a size != 1!!");
            }
            FormUtils.setFormDisplayMode(request, actForm, FormUtils.INSPECT_MODE);
        } catch (Exception e) {
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.user.results.general.image"));
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
            e.printStackTrace();
        }
        if (!errors.isEmpty()) {
            saveErrors(request, errors);
            return (mapping.findForward("error"));
        } else return mapping.findForward("viewJpegImage");
    }
