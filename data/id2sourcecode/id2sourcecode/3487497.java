    public static boolean generateReport(Dao dao, List<Group> list, String outputFile, boolean doBoutNumbers, boolean doTimestamp) {
        if (list.isEmpty()) {
            return false;
        }
        Document document = new Document();
        try {
            FileOutputStream fos = createOutputFile(outputFile);
            if (fos == null) {
                return false;
            }
            PdfWriter writer = PdfWriter.getInstance(document, fos);
            document.open();
            PdfContentByte cb = writer.getDirectContent();
            String timestamp = "";
            if (doTimestamp) {
                timestamp = DateFormat.getInstance().format(new Date());
            }
            int rv;
            int i = 0;
            int size = list.size();
            for (Group g : list) {
                rv = addBracket(cb, dao, g, doBoutNumbers);
                if (rv != PAGE_ERROR) {
                    boolean doWatermark = false;
                    String gClass = g.getClassification();
                    String wmValues = dao.getBracketsheetWatermarkValues();
                    if ((wmValues != null) && !wmValues.isEmpty()) {
                        String[] tokens = wmValues.split(",");
                        for (String s : tokens) {
                            if (s.trim().equalsIgnoreCase(gClass)) {
                                doWatermark = true;
                                break;
                            }
                        }
                    }
                    int rotation = (rv == PAGE_ROUNDROBIN) ? 45 : 135;
                    if (doWatermark) {
                        PdfContentByte ucb = writer.getDirectContentUnder();
                        BaseFont helv = BaseFont.createFont("Helvetica", BaseFont.WINANSI, false);
                        ucb.saveState();
                        ucb.setColorFill(BaseColor.LIGHT_GRAY);
                        ucb.beginText();
                        ucb.setFontAndSize(helv, 86);
                        ucb.showTextAligned(Element.ALIGN_CENTER, gClass, document.getPageSize().getWidth() / 2, document.getPageSize().getHeight() / 2, rotation);
                        ucb.endText();
                        ucb.restoreState();
                    }
                    if (doTimestamp) {
                        rotation -= 45;
                        float width = cb.getPdfWriter().getPageSize().getWidth();
                        int x = (rv == PAGE_ROUNDROBIN) ? 15 : (int) (width - 15);
                        int y = 15;
                        BracketSheetUtil.drawTimestamp(cb, null, x, y, 10, timestamp, rotation);
                    }
                    if (!doBoutNumbers && (dao.getBracketsheetAwardImage() != null) && !dao.getBracketsheetAwardImage().isEmpty()) {
                        Image image = Image.getInstance(Image.getInstance(dao.getBracketsheetAwardImage()));
                        image.setRotationDegrees((rv == PAGE_ROUNDROBIN) ? 0 : 90);
                        PositionOnPage positionOnPage = dao.getBracketsheetAwardImagePosition();
                        if (PositionOnPage.UPPER_RIGHT == positionOnPage) {
                            float x = (rv == PAGE_ROUNDROBIN) ? document.getPageSize().getWidth() - 10 - image.getWidth() : 10;
                            float y = document.getPageSize().getHeight() - 10 - image.getHeight();
                            image.setAbsolutePosition(x, y);
                            cb.addImage(image);
                        } else if (PositionOnPage.CENTER == positionOnPage) {
                            PdfContentByte ucb = writer.getDirectContentUnder();
                            float pageX = document.getPageSize().getWidth() / 2;
                            float pageY = document.getPageSize().getHeight() / 2;
                            float imageX = image.getWidth() / 2;
                            float imageY = image.getHeight() / 2;
                            image.setAbsolutePosition(pageX - imageX, pageY - imageY);
                            ucb.addImage(image);
                        }
                    }
                    if (++i < size) {
                        document.newPage();
                    }
                }
            }
        } catch (DocumentException de) {
            logger.error("Document Exception", de);
            return false;
        } catch (IOException ioe) {
            logger.error("IO Exception", ioe);
            return false;
        }
        document.close();
        return true;
    }
