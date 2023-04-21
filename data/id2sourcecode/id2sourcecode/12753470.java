    @Override
    public String execute() {
        try {
            if ("upload".equals(command)) {
                if (reportFile != null) {
                    File destinationFile = new File(directoryProvider.getReportDirectory() + reportFileFileName);
                    try {
                        if (destinationFile.exists()) {
                            int revisionCount = reportProvider.getReportTemplate(reportFileFileName).getRevisionCount();
                            File versionedFile = new File(directoryProvider.getReportDirectory() + reportFileFileName + "." + revisionCount);
                            FileUtils.copyFile(destinationFile, versionedFile);
                        }
                        FileUtils.copyFile(reportFile, destinationFile);
                    } catch (IOException ioe) {
                        addActionError(ioe.toString());
                        return SUCCESS;
                    }
                    if ((reportFileFileName.endsWith(".xml")) || (reportFileFileName.endsWith(".jrxml"))) {
                        try {
                            System.setProperty(JRProperties.COMPILER_CLASS, JRJdtCompiler.class.getName());
                            JRProperties.setProperty(JRProperties.COMPILER_CLASS, JRJdtCompiler.class.getName());
                            JasperCompileManager.compileReportToFile(destinationFile.getAbsolutePath());
                        } catch (Exception e) {
                            if (e.toString().indexOf("groovy") > -1) {
                                try {
                                    System.setProperty(JRProperties.COMPILER_CLASS, "net.sf.jasperreports.compilers.JRGroovyCompiler");
                                    JRProperties.setProperty(JRProperties.COMPILER_CLASS, "net.sf.jasperreports.compilers.JRGroovyCompiler");
                                    JasperCompileManager.compileReportToFile(destinationFile.getAbsolutePath());
                                } catch (Exception ex) {
                                    log.error("Failed to compile report: " + reportFileFileName, e);
                                    addActionError("Failed to compile report: " + e.toString());
                                }
                            } else {
                                log.error("Failed to compile report: " + reportFileFileName, e);
                                addActionError("Failed to compile report: " + e.toString());
                            }
                        }
                    }
                } else {
                    addActionError("Invalid File.");
                }
            }
            if ("download".equals(command)) {
                String templateFileName = revision;
                if (StringUtils.countMatches(templateFileName, ".") > 1) {
                    templateFileName = revision.substring(0, revision.lastIndexOf("."));
                }
                File templateFile = new File(directoryProvider.getReportDirectory() + revision);
                byte[] template = FileUtils.readFileToByteArray(templateFile);
                HttpServletResponse response = ServletActionContext.getResponse();
                response.setHeader("Content-disposition", "inline; filename=" + templateFileName);
                response.setContentType("application/octet-stream");
                response.setContentLength(template.length);
                ServletOutputStream out = response.getOutputStream();
                out.write(template, 0, template.length);
                out.flush();
                out.close();
            }
            if ("revert".equals(command)) {
                String templateFileName = revision.substring(0, revision.lastIndexOf("."));
                File revisionFile = new File(directoryProvider.getReportDirectory() + revision);
                File currentFile = new File(directoryProvider.getReportDirectory() + templateFileName);
                int revisionCount = reportProvider.getReportTemplate(templateFileName).getRevisionCount();
                File versionedFile = new File(directoryProvider.getReportDirectory() + templateFileName + "." + revisionCount);
                FileUtils.copyFile(currentFile, versionedFile);
                FileUtils.copyFile(revisionFile, currentFile);
            }
            reportTemplates = reportProvider.getReportTemplates();
        } catch (Exception pe) {
            addActionError(pe.getMessage());
        }
        return SUCCESS;
    }
