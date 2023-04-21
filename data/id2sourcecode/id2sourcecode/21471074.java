    private String addFileToZipContent(String path, InputStream inputStream) {
        String res = null;
        WorkflowResultItem wfResultItem = new WorkflowResultItem(WorkflowResultItem.SERVICE_ACTION_IDENTIFICATION, System.currentTimeMillis());
        wfResultItem.addLogInfo("addFileToZipContent");
        try {
            String fileName = path + URI_SEPARATOR + processingDigo.getPermanentUri().toString().substring(processingDigo.getPermanentUri().toString().lastIndexOf(URI_SEPARATOR) + 1);
            wfResultItem.addLogInfo("path: " + path + "fileName: " + fileName);
            File f = new File(fileName);
            OutputStream out = new FileOutputStream(f);
            byte buf[] = new byte[1024];
            int len;
            while ((len = inputStream.read(buf)) > 0) out.write(buf, 0, len);
            out.close();
            inputStream.close();
            res = fileName;
            wfResultItem.addLogInfo("\nFile is created........ res: " + res);
        } catch (Exception e) {
            wfResultItem.addLogInfo("Content file creation error: " + e.getMessage());
        }
        return res;
    }
