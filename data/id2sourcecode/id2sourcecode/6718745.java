    private void serializeVersionOne(OutputStream out) throws Exception {
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
        RemoteUtil.writeLine(addFileTool.getFile().getAbsolutePath(), out);
        RemoteUtil.writeLine(addFileTool.getTitle(), out);
        RemoteUtil.writeLine(addFileTool.getDescription(), out);
        RemoteUtil.writeData(addFileTool.getUserCertificate().getEncoded(), out);
        RemoteUtil.writeData(addFileTool.getUserPrivateKey().getEncoded(), out);
        RemoteUtil.writeLine(addFileTool.getPassphrase(), out);
        RemoteUtil.writeBoolean(addFileTool.isCompress(), out);
        RemoteUtil.writeBoolean(addFileTool.isDataOnly(), out);
        RemoteUtil.writeBoolean(addFileTool.isExplodeBeforeUpload(), out);
        RemoteUtil.writeBoolean(addFileTool.isShowMetaDataIfEncrypted(), out);
        RemoteUtil.writeBoolean(addFileTool.isUsingUnspecifiedServers(), out);
        RemoteUtil.writeInt(addFileTool.getThreadCount(), out);
        RemoteUtil.writeInt(addFileTool.getConfirmationEmails().size(), out);
        for (String email : addFileTool.getConfirmationEmails()) {
            RemoteUtil.writeLine(email, out);
        }
        RemoteUtil.writeInt(addFileTool.getServersToUse().size(), out);
        for (String host : addFileTool.getServersToUse()) {
            RemoteUtil.writeLine(host, out);
        }
        RemoteUtil.writeInt(addFileTool.getStickyServers().size(), out);
        for (String host : addFileTool.getStickyServers()) {
            RemoteUtil.writeLine(host, out);
        }
        RemoteUtil.writeInt(addFileTool.getMetaDataAnnotations().size(), out);
        for (MetaDataAnnotation mda : addFileTool.getMetaDataAnnotations()) {
            RemoteUtil.writeLine(mda.toString(), out);
        }
        RemoteUtil.writeBoolean(addFileTool.getLicense() != null, out);
        if (addFileTool.getLicense() != null) {
            addFileTool.getLicense().serialize(out);
        }
    }
