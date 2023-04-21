    private void storeFile(Channel channel) {
        if (reqType == ArkRequest.Post) {
            if (doc != null && filename != null && metadata != null) {
                try {
                    dkey = doc.storeFromLocalFile(filename, metadata);
                } catch (ArUnvalidIndexException e1) {
                    status = HttpResponseStatus.NOT_ACCEPTABLE;
                    sendError(channel, "While storing from LocalFile: " + filename + ":" + e1.getMessage());
                    return;
                } catch (ArFileException e1) {
                    status = HttpResponseStatus.INTERNAL_SERVER_ERROR;
                    sendError(channel, "While Storing from LocalFile: " + filename + ":" + e1.getMessage());
                    return;
                } catch (ArFileWormException e1) {
                    status = HttpResponseStatus.CONFLICT;
                    sendError(channel, "While Storing from LocalFile: " + filename + ":" + e1.getMessage());
                    return;
                }
                responseFinalContent.setLength(0);
                responseFinalContent.append("<POST>");
                responseFinalContent.append("<LID>");
                responseFinalContent.append(legacy);
                responseFinalContent.append("</LID>");
                responseFinalContent.append("<SID>");
                responseFinalContent.append(store);
                responseFinalContent.append("</SID>");
                responseFinalContent.append("<DID>");
                responseFinalContent.append(document);
                responseFinalContent.append("</DID>");
                responseFinalContent.append("<DKEY>");
                responseFinalContent.append(dkey);
                responseFinalContent.append("</DKEY>");
                responseFinalContent.append("<CTYPE>");
                responseFinalContent.append(contentType);
                responseFinalContent.append("</CTYPE>");
                responseFinalContent.append("</POST>");
            } else {
                status = HttpResponseStatus.NOT_ACCEPTABLE;
                sendError(channel, "Missing Informations(1): " + this.reqType + ":" + (doc != null) + ":" + (filename != null) + ":" + (metadata != null));
                return;
            }
        } else {
            if (doc != null && fileUpload != null && metadata != null) {
                if (fileUpload.isInMemory()) {
                    try {
                        doc.store(Configuration.BLOCKSIZE, metadata);
                        ChannelBuffer buffer = fileUpload.getChannelBuffer();
                        DataBlock dataBlock = new DataBlock();
                        dataBlock.setBlock(buffer);
                        dataBlock.setEOF(true);
                        dkey = doc.writeDataBlock(dataBlock);
                    } catch (ArUnvalidIndexException e1) {
                        fileUpload.delete();
                        status = HttpResponseStatus.NOT_ACCEPTABLE;
                        sendError(channel, "While Storing from UploadFile: " + e1.getMessage());
                        return;
                    } catch (ArFileException e1) {
                        fileUpload.delete();
                        status = HttpResponseStatus.INTERNAL_SERVER_ERROR;
                        sendError(channel, "While Storing from UploadFile: " + e1.getMessage());
                        return;
                    } catch (ArFileWormException e1) {
                        fileUpload.delete();
                        status = HttpResponseStatus.CONFLICT;
                        sendError(channel, "While Storing from UploadFile: " + e1.getMessage());
                        return;
                    } catch (IOException e1) {
                        fileUpload.delete();
                        status = HttpResponseStatus.INTERNAL_SERVER_ERROR;
                        sendError(channel, "While Storing from UploadFile: " + e1.getMessage());
                        return;
                    }
                } else {
                    try {
                        dkey = doc.storeFromLocalFile(fileUpload.getFile().getAbsolutePath(), metadata);
                    } catch (ArUnvalidIndexException e1) {
                        fileUpload.delete();
                        status = HttpResponseStatus.NOT_ACCEPTABLE;
                        sendError(channel, "While Storing from UploadFile: " + e1.getMessage());
                        return;
                    } catch (ArFileException e1) {
                        fileUpload.delete();
                        status = HttpResponseStatus.INTERNAL_SERVER_ERROR;
                        sendError(channel, "While Storing from UploadFile: " + e1.getMessage());
                        return;
                    } catch (ArFileWormException e1) {
                        fileUpload.delete();
                        status = HttpResponseStatus.CONFLICT;
                        sendError(channel, "While Storing from UploadFile: " + e1.getMessage());
                        return;
                    } catch (IOException e1) {
                        fileUpload.delete();
                        status = HttpResponseStatus.INTERNAL_SERVER_ERROR;
                        sendError(channel, "While Storing from UploadFile: " + e1.getMessage());
                        return;
                    }
                }
                fileUpload.delete();
                responseFinalContent.setLength(0);
                responseFinalContent.append("<POSTUPLOAD>");
                responseFinalContent.append("<LID>");
                responseFinalContent.append(legacy);
                responseFinalContent.append("</LID>");
                responseFinalContent.append("<SID>");
                responseFinalContent.append(store);
                responseFinalContent.append("</SID>");
                responseFinalContent.append("<DID>");
                responseFinalContent.append(document);
                responseFinalContent.append("</DID>");
                responseFinalContent.append("<DKEY>");
                responseFinalContent.append(dkey);
                responseFinalContent.append("</DKEY>");
                responseFinalContent.append("<CTYPE>");
                responseFinalContent.append(contentType);
                responseFinalContent.append("</CTYPE>");
                responseFinalContent.append("</POSTUPLOAD>");
            } else {
                if (fileUpload != null) fileUpload.delete();
                status = HttpResponseStatus.NOT_ACCEPTABLE;
                sendError(channel, "Missing Informations(2): " + this.reqType + ":" + (doc != null) + ":" + (fileUpload != null) + ":" + (metadata != null));
                return;
            }
        }
        status = HttpResponseStatus.OK;
        writeResponse(channel);
    }
