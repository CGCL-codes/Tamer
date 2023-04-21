    public void serialize(OutputStream out) throws Exception {
        RemoteUtil.writeInt(version, out);
        RemoteUtil.writeLine(status, out);
        RemoteUtil.writeInt(actionMap.size(), out);
        for (String key : actionMap.keySet()) {
            RemoteUtil.writeLine(key, out);
            RemoteUtil.writeInt(actionMap.get(key).size(), out);
            for (String key2 : actionMap.get(key).keySet()) {
                RemoteUtil.writeLine(key2, out);
                RemoteUtil.writeInt(actionMap.get(key).get(key2), out);
            }
        }
        RemoteUtil.writeBoolean(report != null, out);
        if (report != null) {
            report.serialize(out);
        }
        RemoteUtil.writeBigHash(getFileTool.getHash(), out);
        RemoteUtil.writeLine(getFileTool.getPassphrase(), out);
        RemoteUtil.writeLine(getFileTool.getSaveFile().getAbsolutePath(), out);
        RemoteUtil.writeLine(getFileTool.getRegEx(), out);
        RemoteUtil.writeLine(getFileTool.getUploadRelativePath(), out);
        RemoteUtil.writeLine(getFileTool.getUploaderName(), out);
        RemoteUtil.writeLong(getFileTool.getUploadTimestamp(), out);
        RemoteUtil.writeInt(getFileTool.getThreadCount(), out);
        RemoteUtil.writeBoolean(getFileTool.isBatch(), out);
        RemoteUtil.writeBoolean(getFileTool.isContinueOnFailure(), out);
        RemoteUtil.writeBoolean(getFileTool.isUsingUnspecifiedServers(), out);
        RemoteUtil.writeBoolean(getFileTool.isValidate(), out);
        RemoteUtil.writeInt(getFileTool.getServersToUse().size(), out);
        for (String host : getFileTool.getServersToUse()) {
            RemoteUtil.writeLine(host, out);
        }
        RemoteUtil.writeLong(bytesToDownload, out);
        RemoteUtil.writeLong(filesToDownload, out);
    }
