    private boolean exportAsJPG() throws CridmanagerException {
        if (logger.isDebugEnabled()) {
            logger.debug("exportAsJPG() - start");
        }
        OutputStream out = null;
        IFile destinationJPG = null;
        Collection textPagesDesc;
        Collection textPagesType;
        Collection textPagesTitle;
        float drawPosY = 100;
        float drawPosXMargin = 70;
        float drawPosX = 0;
        Point imageSize = new Point(720, 576);
        int spaceTitle_Type = 35;
        int spaceType_Desc = 20;
        BufferedImage image;
        try {
            Font fontTitle = new Font(Messages.getString("SaveEPGInfo.JpegFont"), Font.BOLD, 29);
            Font fontType = new Font(Messages.getString("SaveEPGInfo.JpegFont"), Font.BOLD | Font.ITALIC, 18);
            Font fontDesc = new Font(Messages.getString("SaveEPGInfo.JpegFont"), Font.PLAIN, 18);
            {
                image = new BufferedImage(imageSize.x, imageSize.y, BufferedImage.TYPE_INT_RGB);
                Graphics2D g2D = image.createGraphics();
                FontRenderContext frc = g2D.getFontRenderContext();
                textPagesTitle = splitTextToPages(new StringBuffer(info.getCridTitel()), fontTitle, drawPosY);
                if (settings.isSet(ISettings.SAVE_EPGINFO_TITLE)) {
                    TextLayout layout = new TextLayout("Teststring", fontTitle, frc);
                    drawPosY += (layout.getAscent() + layout.getDescent() + layout.getLeading()) * countLines(textPagesTitle);
                }
                drawPosY += spaceTitle_Type;
                textPagesType = splitTextToPages(new StringBuffer(info.getCridFilmTypeYear()), fontType, drawPosY);
                if (settings.isSet(ISettings.SAVE_EPGINFO_YEAR)) {
                    TextLayout layout = new TextLayout("Teststring", fontType, frc);
                    drawPosY += (layout.getAscent() + layout.getDescent() + layout.getLeading()) * countLines(textPagesType);
                }
                drawPosY += spaceType_Desc;
                textPagesDesc = splitTextToPages(new StringBuffer(info.getCridDescription()), fontDesc, drawPosY);
            }
            Iterator iteratorDesc = textPagesDesc.iterator();
            int counter = 0;
            do {
                if (counter == 0) destinationJPG = path.create(fileName + ".jpg"); else destinationJPG = path.create(fileName + "_" + String.valueOf(counter) + ".jpg");
                out = new BufferedOutputStream(destinationJPG.createOutput());
                image = new BufferedImage(imageSize.x, imageSize.y, BufferedImage.TYPE_INT_RGB);
                Graphics2D g2D = image.createGraphics();
                g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2D.setColor(new Color(4, 9, 111));
                g2D.fillRect(0, 0, image.getWidth(), image.getHeight());
                drawPosY = 100;
                if (settings.isSet(ISettings.SAVE_EPGINFO_TITLE)) {
                    if (info.getCridTitel().length() > 0) {
                        g2D.setFont(fontTitle);
                        g2D.setColor(new Color(255, 255, 255));
                        Iterator iteratorTitle = textPagesTitle.iterator();
                        drawPosY = paintLines2Graphic(g2D, iteratorTitle, fontTitle, imageSize, new Point2D.Float(drawPosX, drawPosY), drawPosXMargin, CENTER, UNDERLINE);
                        drawPosY += spaceTitle_Type;
                    }
                }
                if (settings.isSet(ISettings.SAVE_EPGINFO_YEAR)) {
                    if (info.getCridFilmTypeYear().length() > 0) {
                        g2D.setFont(fontType);
                        g2D.setColor(new Color(255, 255, 255));
                        Iterator iteratorType = textPagesType.iterator();
                        drawPosY = paintLines2Graphic(g2D, iteratorType, fontType, imageSize, new Point2D.Float(drawPosX, drawPosY), drawPosXMargin, CENTER, NO_UNDERLINE);
                        drawPosY += spaceType_Desc;
                    }
                }
                if (settings.isSet(ISettings.SAVE_EPGINFO_DESCRIPTION)) {
                    g2D.setFont(fontDesc);
                    g2D.setColor(new Color(200, 200, 200));
                    drawPosY = paintLines2Graphic(g2D, iteratorDesc, fontDesc, imageSize, new Point2D.Float(drawPosX, drawPosY), drawPosXMargin, LEFT, NO_UNDERLINE);
                }
                String channelTime = "";
                if (settings.isSet(ISettings.SAVE_EPGINFO_BROADCASTSTATION)) {
                    channelTime = getChannelName();
                }
                if (settings.isSet(ISettings.SAVE_EPGINFO_DATE)) {
                    if (settings.isSet(ISettings.SAVE_EPGINFO_BROADCASTSTATION)) channelTime += ", ";
                    channelTime += date2shortString();
                }
                if (channelTime != "") {
                    g2D.setFont(fontDesc);
                    g2D.setColor(new Color(200, 200, 200));
                    drawPosY += 1.5 * fontDesc.getSize2D();
                    g2D.drawString(channelTime, drawPosXMargin, drawPosY);
                }
                ByteArrayOutputStream byteArrayOutput = new ByteArrayOutputStream(0xfff);
                JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(byteArrayOutput);
                JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(image);
                param.setQuality((float) 0.95, true);
                encoder.encode(image, param);
                out.write(byteArrayOutput.toByteArray());
                out.flush();
                destinationJPG.close();
                counter++;
            } while (iteratorDesc.hasNext());
        } catch (Exception e) {
            logger.error("exportAsJPG()", e);
            if (e instanceof FileNotFoundException) {
                errorTxt = Messages.getString("SaveEPGInfo.CantCreateFile");
            } else {
                errorTxt = e.getMessage();
            }
            String dest = destination == null ? Messages.getString("Global.Unbekannt") : destination.getAbsolutePath();
            String crid = info == null ? Messages.getString("Global.Unbekannt") : info.getFileName();
            throw new CridmanagerException(new StringBuffer(Messages.getString("SaveEPGInfo.SaveErrorMessage") + ", dest= " + dest + ", crid= " + crid + "). "), e);
        } finally {
            if (out != null) destinationJPG.close();
        }
        if (logger.isDebugEnabled()) {
            logger.debug("exportAsJPG() - end");
        }
        return true;
    }
