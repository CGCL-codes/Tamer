    public boolean bnOkMethod() {
        System.out.println("hashtable ====================== " + htAttachments);
        if (htAttachments != null) {
            Enumeration enumeration = htAttachments.keys();
            int noOfFiles = 0;
            Hashtable htAttachmentData = new Hashtable();
            String oldLocation = this.getFileLocation(htAttachments);
            String fileLocation = oldLocation;
            Vector vFailedAttachments = new Vector();
            while (enumeration.hasMoreElements()) {
                String id = utility.getTestedString(enumeration.nextElement());
                Hashtable htAttachment = (Hashtable) htAttachments.get(id);
                if (htAttachment != null) {
                    id = utility.getTestedString(htAttachment.get("Id"));
                    String libraryId = utility.getTestedString(htAttachment.get("LibraryId"));
                    String catalogueId = utility.getTestedString(htAttachment.get("CatalogueId"));
                    String fileName = utility.getTestedString(htAttachment.get("FileName"));
                    String filePath = utility.getTestedString(htAttachment.get("FilePath"));
                    String extension = utility.getTestedString(htAttachment.get("Extension"));
                    String displayName = utility.getTestedString(htAttachment.get("DisplayName"));
                    String description = utility.getTestedString(htAttachment.get("Description"));
                    String categoryId = utility.getTestedString(htAttachment.get("CategoryId"));
                    String transactionLog = utility.getTestedString(htAttachment.get("TransactionLog"));
                    String status = utility.getTestedString(htAttachment.get("Status"));
                    if (status.equalsIgnoreCase("L")) {
                        String xmlRequest = this.getRequestXML(libraryId, catalogueId, fileName, displayName, description, extension, categoryId, fileLocation, transactionLog);
                        String response = "";
                        if (status.equalsIgnoreCase("SUCCESS")) {
                            id = this.getId(response);
                            if (!id.equalsIgnoreCase("")) {
                                htAttachmentData.put("Id", id);
                                htAttachmentData.put("CatalogueId", catalogueId);
                                htAttachmentData.put("Process", "U");
                                htAttachmentData.put("FileName", fileName);
                                oldLocation = fileLocation;
                                htAttachmentData.put("OldLocation", oldLocation);
                                File file = new File(filePath);
                                try {
                                    FileChannel readChannel = new FileInputStream(file).getChannel();
                                    long size = readChannel.size();
                                    if (size < Integer.MAX_VALUE) {
                                        int offset = 0;
                                        boolean flagStatus = true;
                                        while (offset < size && flagStatus) {
                                            byte[] bytes = null;
                                            if (offset < size - (1024 * 100)) {
                                                bytes = new byte[1024 * 100];
                                                readChannel.map(FileChannel.MapMode.READ_ONLY, offset, 1024 * 100).get(bytes);
                                            } else {
                                                bytes = new byte[(int) size - offset];
                                                readChannel.map(FileChannel.MapMode.READ_ONLY, offset, size - offset).get(bytes);
                                            }
                                            htAttachmentData.put("FileLocation", fileLocation);
                                            htAttachmentData.put("Bytes", bytes);
                                            Hashtable htResponse = (Hashtable) ServletConnector.getInstance().sendObjectRequest("NGLDigitalAttachmentsHandler", htAttachmentData);
                                            if (htResponse != null) {
                                                status = utility.getTestedStringWithTrim(htResponse.get("Status"));
                                                System.out.println("status ========================= " + status);
                                                if (status.equalsIgnoreCase("SUCCESS")) {
                                                    String newLocation = utility.getTestedString(htResponse.get("FileLocation"));
                                                    System.out.println("newLocation ===================== " + newLocation + ", fileLocation ================= " + fileLocation);
                                                    if (!newLocation.equalsIgnoreCase(fileLocation)) {
                                                        xmlRequest = getRequestXML(libraryId, catalogueId, newLocation);
                                                        if (this.getStatus(response).equalsIgnoreCase("SUCCESS")) {
                                                            fileLocation = newLocation;
                                                        } else {
                                                            htAttachmentData.put("Process", "DR");
                                                            htAttachmentData.put("SourceLocation", newLocation);
                                                            htAttachmentData.put("RestoreLocation", fileLocation);
                                                            htResponse = (Hashtable) ServletConnector.getInstance().sendObjectRequest("NGLDigitalAttachmentsHandler", htAttachmentData);
                                                            flagStatus = false;
                                                        }
                                                    }
                                                } else {
                                                    xmlRequest = getRequestXML(id, libraryId, catalogueId, oldLocation);
                                                    fileLocation = oldLocation;
                                                    flagStatus = false;
                                                }
                                            } else {
                                                xmlRequest = getRequestXML(id, libraryId, catalogueId, oldLocation);
                                                fileLocation = oldLocation;
                                                flagStatus = false;
                                            }
                                            offset += 1024 * 100;
                                        }
                                        if (!flagStatus) {
                                            noOfFiles++;
                                            vFailedAttachments.addElement(htAttachment);
                                        } else {
                                            System.out.println("Updating status..............");
                                            System.out.println("id======" + id + ", libraryId ======= " + libraryId);
                                            xmlRequest = getRequestXML(id, libraryId, catalogueId, "", "S");
                                            System.out.println("xmlRequest ================== " + xmlRequest);
                                            System.out.println("response ============================ " + response);
                                            if (!getStatus(response).equalsIgnoreCase("SUCCESS")) {
                                                noOfFiles++;
                                                vFailedAttachments.addElement(htAttachment);
                                            } else {
                                                System.out.println("Failed to update status...........");
                                            }
                                        }
                                    } else {
                                        panelOutSugg.setMessage(0, 1, "File is too large");
                                    }
                                } catch (Exception ex) {
                                    System.out.println("Exception ============== " + ex);
                                }
                            }
                        }
                    } else if (status.equalsIgnoreCase("M")) {
                    } else if (status.equalsIgnoreCase("D")) {
                    }
                }
            }
            if (noOfFiles > 0) {
                panelOutSugg.setMessage(0, 1, "Failed to upload " + noOfFiles + " file(s)");
            } else {
                return true;
            }
        }
        return false;
    }
