    public void extractImagesUsingPdfObjectAccess(String pdfFile, String password, Set<Integer> pagesToProcess, Boolean silent, Boolean binarize) throws PdfRecompressionException {
        if (binarize == null) {
            binarize = false;
        }
        if (pdfFile == null) {
            throw new IllegalArgumentException(pdfFile);
        }
        String prefix = null;
        InputStream inputStream = null;
        if (password != null) {
            try {
                ByteArrayOutputStream decryptedOutputStream = null;
                PdfReader reader = new PdfReader(pdfFile, password.getBytes());
                PdfStamper stamper = new PdfStamper(reader, decryptedOutputStream);
                stamper.close();
                inputStream = new ByteArrayInputStream(decryptedOutputStream.toByteArray());
            } catch (DocumentException ex) {
                throw new PdfRecompressionException(ex);
            } catch (IOException ex) {
                throw new PdfRecompressionException("Reading file caused exception", ex);
            }
        } else {
            try {
                inputStream = new FileInputStream(pdfFile);
            } catch (FileNotFoundException ex) {
                throw new PdfRecompressionException("File wasn't found", ex);
            }
        }
        if ((prefix == null) && (pdfFile.length() > 4)) {
            prefix = pdfFile.substring(0, pdfFile.length() - 4);
        }
        PDFParser parser = null;
        PDDocument doc = null;
        try {
            parser = new PDFParser(inputStream);
            parser.parse();
            doc = parser.getPDDocument();
            AccessPermission accessPermissions = doc.getCurrentAccessPermission();
            if (!accessPermissions.canExtractContent()) {
                throw new PdfRecompressionException("Error: You do not have permission to extract images.");
            }
            List pages = doc.getDocumentCatalog().getAllPages();
            for (int pageNumber = 0; pageNumber < pages.size(); pageNumber++) {
                if ((pagesToProcess != null) && (!pagesToProcess.contains(pageNumber + 1))) {
                    continue;
                }
                PDPage page = (PDPage) pages.get(pageNumber);
                PDResources resources = page.getResources();
                Map xobjs = resources.getXObjects();
                if (xobjs != null) {
                    Iterator xobjIter = xobjs.keySet().iterator();
                    while (xobjIter.hasNext()) {
                        String key = (String) xobjIter.next();
                        PDXObject xobj = (PDXObject) xobjs.get(key);
                        Map images;
                        if (xobj instanceof PDXObjectForm) {
                            PDXObjectForm xform = (PDXObjectForm) xobj;
                            images = xform.getResources().getImages();
                        } else {
                            images = resources.getImages();
                        }
                        if (images != null) {
                            Iterator imageIter = images.keySet().iterator();
                            while (imageIter.hasNext()) {
                                String imKey = (String) imageIter.next();
                                PDXObjectImage image = (PDXObjectImage) images.get(imKey);
                                PDStream pdStr = new PDStream(image.getCOSStream());
                                List filters = pdStr.getFilters();
                                if (image.getBitsPerComponent() > 1) {
                                    log.info("It is not a bitonal image => skipping");
                                    continue;
                                }
                                if (filters.contains(COSName.LZW_DECODE.getName())) {
                                    log.info("This is LZWDecoded => skipping");
                                    continue;
                                }
                                if (filters.contains("JBIG2Decode")) {
                                    log.info("Allready compressed according to JBIG2 standard => skipping");
                                    continue;
                                }
                                if (filters.contains("JPXDecode")) {
                                    log.info("Unsupported filter JPXDecode => skipping");
                                    continue;
                                }
                                COSObject cosObj = new COSObject(image.getCOSObject());
                                int objectNum = cosObj.getObjectNumber().intValue();
                                int genNum = cosObj.getGenerationNumber().intValue();
                                log.debug(objectNum + " " + genNum + " obj");
                                String name = getUniqueFileName(prefix + imKey, image.getSuffix());
                                log.debug("Writing image:" + name);
                                image.write2file(name);
                                PdfImageInformation pdfImageInfo = new PdfImageInformation(key, image.getWidth(), image.getHeight(), objectNum, genNum);
                                originalImageInformations.add(pdfImageInfo);
                                log.debug(pdfImageInfo.toString());
                                namesOfImages.add(name + "." + image.getSuffix());
                            }
                        }
                    }
                }
            }
        } catch (IOException ex) {
            throw new PdfRecompressionException("Unable to parse PDF document", ex);
        } finally {
            if (doc != null) {
                try {
                    doc.close();
                } catch (IOException ex) {
                    throw new PdfRecompressionException(ex);
                }
            }
        }
    }
