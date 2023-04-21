    @RequestMapping(value = { "/updatestudy" })
    public ModelAndView updateStudy(@RequestParam("file") MultipartFile file, @RequestParam(required = true, value = "study") String study, @RequestParam(required = false, value = "pickdate") String publicReleaseDateS, HttpServletRequest request) throws Exception {
        logger.info("Starting Updating study " + study);
        RequestParameters params = new RequestParameters(publicReleaseDateS, study, file);
        ModelAndView validation = validateParameters(params);
        if (validation != null) {
            return validation;
        }
        File backup = new File(uploadDirectory + "backup/" + study + ".zip");
        boolean needRestore = false;
        try {
            File isaTabFile = new File(submissionController.writeFile(file, null));
            IsaTabUploader itu = submissionController.getIsaTabUploader(isaTabFile.getAbsolutePath(), params.status, params.publicReleaseDateS);
            Map<String, String> zipValues = itu.getStudyFields(isaTabFile, new String[] { "Study Identifier" });
            String newStudyId = zipValues.get("Study Identifier");
            if (!study.equals(newStudyId)) {
                validation = getModelAndView(study, true);
                validation.addObject("validationmsg", PropertyLookup.getMessage("msg.validation.studyIdDoNotMatch", newStudyId, study));
                return validation;
            }
            if (backup.exists()) {
                throw new Exception(PropertyLookup.getMessage("msg.validation.backupFileExists", study));
            }
            try {
                itu.validate(itu.getUnzipFolder());
            } catch (Exception e) {
                validation = getModelAndView(study, true);
                validation.addObject("validationmsg", PropertyLookup.getMessage("msg.validation.invalid"));
                List<TabLoggingEventWrapper> isaTabLog = itu.getSimpleManager().getLastLog();
                validation.addObject("isatablog", isaTabLog);
                return validation;
            }
            File currentFile = new File(itu.getStudyFilePath(study, VisibilityStatus.PRIVATE));
            FileUtils.copyFile(currentFile, backup);
            logger.info("Deleting previous study " + study);
            itu.unloadISATabFile(study);
            needRestore = true;
            itu.setIsaTabFile(itu.getUnzipFolder());
            logger.info("Uploading new study");
            itu.UploadWithoutIdReplacement(study);
            needRestore = false;
            backup.delete();
            ModelAndView mav = new ModelAndView("updateStudyForm");
            mav.addObject("title", PropertyLookup.getMessage("msg.updatestudy.ok.title", study));
            mav.addObject("message", PropertyLookup.getMessage("msg.updatestudy.ok.msg", study));
            mav.addObject("searchResult", getStudy(study));
            mav.addObject("updated", true);
            return mav;
        } catch (Exception e) {
            if (needRestore) {
                VisibilityStatus oldStatus = params.study.getIsPublic() ? VisibilityStatus.PUBLIC : VisibilityStatus.PRIVATE;
                IsaTabUploader itu = submissionController.getIsaTabUploader(backup.getAbsolutePath(), oldStatus, null);
                itu.UploadWithoutIdReplacement(study);
                backup.delete();
                throw new Exception("There was an error while updating the study. We have restored the previous experiment. " + e.getMessage());
            } else {
                throw e;
            }
        }
    }
