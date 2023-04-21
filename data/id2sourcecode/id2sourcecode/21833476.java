    @Override
    public void put(RapidData data, FileItem item) throws RapidException {
        OutputStream outputStream = null;
        InputStream inputStream = null;
        FileObject fileObject = null;
        JobData jobData = data.getJobData();
        FileSystemTable fileSystemTable = data.getFileSystemTable();
        try {
            if (item.isFormField()) throw new RapidException("Item " + item.getFieldName() + " is not a File at symbol " + this.getVariableName());
            if (!"".equals(item.getName())) {
                if (!"".equals(this.getVariableName())) VariableResolver.getVariable(this.getVariableName(), jobData).put(item.getName());
                String resolvedFileSystem = VariableResolver.resolve(this.fileSystemName, jobData);
                AbstractFileSystem fileSystem = fileSystemTable.getFileSystem(resolvedFileSystem);
                fileObject = fileSystemTable.getFileSystemConnector().connect(fileSystem, jobData, 0);
                String resolvedPath = VariableResolver.resolve(this.path, jobData);
                fileObject = fileObject.resolveFile(resolvedPath);
                if (fileObject.exists() && fileObject.getType() == FileType.FOLDER) {
                    fileObject = fileObject.resolveFile(item.getName());
                    fileObject.createFile();
                } else if (fileObject.exists() && fileObject.getType() != FileType.FOLDER) {
                    fileObject.delete();
                    fileObject.createFile();
                } else {
                    fileObject.createFile();
                }
                outputStream = fileObject.getContent().getOutputStream();
                inputStream = item.getInputStream();
                byte[] buffer = new byte[1024];
                int size = 0;
                while ((size = inputStream.read(buffer)) != -1) outputStream.write(buffer, 0, size);
            }
        } catch (Exception ex) {
            throw new RapidException("Unable to write file to " + this.fileSystemName + " : " + this.path + " cause: " + ex.getMessage());
        } finally {
            try {
                if (fileObject != null) fileObject.close();
                if (inputStream != null) inputStream.close();
                if (outputStream != null) outputStream.close();
            } catch (Exception ex) {
                throw new RapidException("Unable to close file " + this.fileSystemName + " : " + this.path + " cause: " + ex.getMessage());
            }
        }
    }
