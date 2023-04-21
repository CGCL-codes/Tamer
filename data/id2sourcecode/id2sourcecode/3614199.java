    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        String dicomURL = "http://" + ((ServerConfiguration) (getServletContext().getAttribute("serverConfig"))).getHostName() + ":" + ((ServerConfiguration) (getServletContext().getAttribute("serverConfig"))).getWadoPort() + "/wado?requestType=WADO&";
        String seriesUID = request.getParameter("series");
        String objectUID = request.getParameter("object");
        String studyUID = request.getParameter("study");
        dicomURL += "contentType=application/dicom&studyUID=" + studyUID + "&seriesUID=" + seriesUID + "&objectUID=" + objectUID + "&transferSyntax=1.2.840.10008.1.2.1";
        dicomURL = dicomURL.replace("+", "%2B");
        InputStream is = null;
        DicomInputStream dis = null;
        try {
            URL url = new URL(dicomURL);
            is = url.openStream();
            dis = new DicomInputStream(is);
            DicomObject dob = dis.readDicomObject();
            DicomElement sopClassUID = dob.get(Tag.SOPClassUID);
            DicomElement nativeRows = dob.get(Tag.Rows);
            DicomElement nativeColumns = dob.get(Tag.Columns);
            DicomElement windowCenter = dob.get(Tag.WindowCenter);
            DicomElement windowWidth = dob.get(Tag.WindowWidth);
            String sopClassUIDValue = sopClassUID == null ? null : new String(sopClassUID.getBytes()).trim();
            int nativeRowsValueNum = nativeRows == null ? 0 : nativeRows.getInt(false);
            int nativeColumnsValueNum = nativeColumns == null ? 0 : nativeColumns.getInt(false);
            String windowCenterValue = windowCenter == null ? null : new String(windowCenter.getBytes());
            String windowWidthValue = windowWidth == null ? null : new String(windowWidth.getBytes());
            String pixelSpaceAttributeName = null;
            String pixelMessage = null;
            DicomElement spacing = null;
            DicomElement imagerSpacing = null;
            DicomElement pixelSpacing = null;
            if ((sopClassUID != null) && (sopClassUIDValue.equals(CT) || sopClassUIDValue.equals(MR))) {
                spacing = dob.get(Tag.PixelSpacing);
                pixelSpaceAttributeName = "Pixel Spacing";
            } else if ((sopClassUID != null) && (sopClassUIDValue.equals(CR) || sopClassUIDValue.equals(XA))) {
                pixelSpacing = dob.get(Tag.PixelSpacing);
                imagerSpacing = dob.get(Tag.ImagerPixelSpacing);
                String pixelSpacingStr = getDcmStrAttrVal(pixelSpacing);
                if (!pixelSpacingStr.equals("")) {
                    String imagerSpacingStr = getDcmStrAttrVal(imagerSpacing);
                    if (!imagerSpacingStr.equals("")) {
                        if (imagerSpacingStr.equals(pixelSpacingStr)) {
                            spacing = imagerSpacing;
                            pixelSpaceAttributeName = "Imager Pixel Spacing";
                            pixelMessage = "Measurements are at the detector plane.";
                        } else {
                            spacing = pixelSpacing;
                            pixelSpaceAttributeName = "Pixel Spacing";
                            pixelMessage = "Measurement has been calibrated, details: " + getCalibrationDetails(dob);
                        }
                    } else {
                        spacing = pixelSpacing;
                        pixelSpaceAttributeName = "Pixel Spacing";
                        pixelMessage = "Warning: Measurement MAY have been calibrated, details: " + getCalibrationDetails(dob);
                        pixelMessage += " It is not clear what this measurement represents.";
                    }
                } else {
                    spacing = imagerSpacing;
                    pixelSpaceAttributeName = "Imager Pixel Spacing";
                    pixelMessage = "Measurements are at the detector plane.";
                }
            }
            String spacingValue = spacing == null ? null : new String(spacing.getBytes());
            double xSpacingValueNum = 0;
            double ySpacingValueNum = 0;
            double windowWidthValueNum = 0;
            double windowCenterValueNum = 0;
            if ((windowCenter != null) && (windowCenter.vm(null) == 2)) {
                windowCenterValueNum = new Double(windowCenterValue.split("\\\\")[0].trim()).doubleValue();
            } else if (windowCenter != null) {
                windowCenterValueNum = new Double(windowCenterValue.trim());
            }
            if ((windowWidth != null) && (windowWidth.vm(null) == 2)) {
                windowWidthValueNum = new Double(windowWidthValue.split("\\\\")[0].trim()).doubleValue();
            } else if (windowWidth != null) {
                windowWidthValueNum = new Double(windowWidthValue.trim()).doubleValue();
            }
            if ((spacing != null) && (spacing.vm(null) == 2)) {
                String[] spacingValues = spacingValue.split("\\\\");
                ySpacingValueNum = new Double(spacingValues[0].trim()).doubleValue();
                xSpacingValueNum = new Double(spacingValues[1].trim()).doubleValue();
            } else if ((spacing != null) && (spacing.vm(null) == 1)) {
                ySpacingValueNum = new Double(spacingValue.trim()).doubleValue();
                xSpacingValueNum = ySpacingValueNum;
            }
            dis.close();
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put(WINDOW_CENTER_PARAM, windowCenterValueNum);
            jsonResponse.put(WINDOW_WIDTH_PARAM, windowWidthValueNum);
            jsonResponse.put(X_PIXEL_SPACING, xSpacingValueNum);
            jsonResponse.put(Y_PIXEL_SPACING, ySpacingValueNum);
            jsonResponse.put(PIXEL_SPACING_ATTRIBUTE, pixelSpaceAttributeName);
            jsonResponse.put(PIXEL_MESSAGE, pixelMessage);
            jsonResponse.put(NATIVE_ROWS, nativeRowsValueNum);
            jsonResponse.put(NATIVE_COLUMNS, nativeColumnsValueNum);
            jsonResponse.put("status", "success");
            is.close();
            dis.close();
            out.println(jsonResponse.toString());
            out.close();
        } catch (Exception e) {
            is.close();
            dis.close();
            out.println("{\"status\":\"error\"}");
            out.close();
            System.out.println(e);
            log.error("Unable to read and send the DICOM dataset page", e);
        }
    }
