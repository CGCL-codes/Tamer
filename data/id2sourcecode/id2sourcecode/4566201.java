    public ActionForward printPoster(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Document document = new Document(PageSize.LETTER, 50, 50, 50, 50);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, baos);
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            Person person = (Person) personService.getPersonById(id);
            String tokens[] = person.getPhoto().split("\\/");
            String defaultPhotoBasename = "";
            for (int i = 0; i < tokens.length - 1; i++) {
                defaultPhotoBasename += tokens[i] + File.separator;
            }
            defaultPhotoBasename += tokens[tokens.length - 1];
            String absoluteDefaultPhotoFilename = getServlet().getServletContext().getRealPath("/") + defaultPhotoBasename;
            document.addTitle("Poster");
            document.addAuthor("OpenMPIS");
            document.addSubject("Poster for " + person.getNickname());
            document.addKeywords("OpenMPIS, missing, found, unidentified");
            document.addProducer();
            document.addCreationDate();
            document.addCreator("OpenMPIS version " + Constants.VERSION);
            document.open();
            if (person.getType() > 4) {
                Paragraph foundParagraph = new Paragraph("F O U N D", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 36, Font.BOLD, new Color(255, 0, 0)));
                foundParagraph.setAlignment(Paragraph.ALIGN_CENTER);
                document.add(foundParagraph);
            } else {
                Paragraph missingParagraph = new Paragraph("M I S S I N G", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 36, Font.BOLD, new Color(255, 0, 0)));
                missingParagraph.setAlignment(Paragraph.ALIGN_CENTER);
                document.add(missingParagraph);
            }
            Paragraph blackParagraph = new Paragraph(getResources(request).getMessage("month." + person.getMonthMissingOrFound()) + " " + person.getDayMissingOrFound() + ", " + person.getYearMissingOrFound(), FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD, new Color(0, 0, 0)));
            blackParagraph.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(blackParagraph);
            if (person.getType() < 5) {
                blackParagraph = new Paragraph(person.getMissingFromCity() + ", " + person.getMissingFromProvince(), FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD, new Color(0, 0, 0)));
                blackParagraph.setAlignment(Paragraph.ALIGN_CENTER);
                document.add(blackParagraph);
            }
            Paragraph redParagraph;
            if (!person.getNickname().isEmpty()) {
                redParagraph = new Paragraph(person.getFirstName() + " \"" + person.getNickname() + "\" " + person.getLastName(), FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD, new Color(255, 0, 0)));
            } else {
                redParagraph = new Paragraph(person.getFirstName() + " " + person.getLastName(), FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD, new Color(255, 0, 0)));
            }
            redParagraph.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(redParagraph);
            Image image = Image.getInstance(absoluteDefaultPhotoFilename);
            image.scaleAbsolute(200, 300);
            image.setAlignment(Image.ALIGN_CENTER);
            document.add(image);
            redParagraph = new Paragraph("Description", FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD, new Color(255, 0, 0)));
            redParagraph.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(redParagraph);
            float[] widths = { 0.5f, 0.5f };
            PdfPTable pdfptable = new PdfPTable(widths);
            pdfptable.setWidthPercentage(100);
            if (person.getType() < 5) {
                pdfptable.addCell(new Phrase(getResources(request).getMessage("label.date.birth") + ": " + getResources(request).getMessage("month." + person.getBirthMonth()) + " " + person.getBirthDay() + ", " + person.getBirthYear(), FontFactory.getFont(FontFactory.HELVETICA, 12)));
                pdfptable.addCell(new Phrase(getResources(request).getMessage("label.address.city") + ": " + person.getCity(), FontFactory.getFont(FontFactory.HELVETICA, 12)));
            }
            pdfptable.addCell(new Phrase(getResources(request).getMessage("label.sex") + ": " + getResources(request).getMessage("sex." + person.getSex()), FontFactory.getFont(FontFactory.HELVETICA, 12)));
            pdfptable.addCell(new Phrase(getResources(request).getMessage("label.color.hair") + ": " + getResources(request).getMessage("color.hair." + person.getHairColor()), FontFactory.getFont(FontFactory.HELVETICA, 12)));
            pdfptable.addCell(new Phrase(getResources(request).getMessage("label.height") + ": " + person.getFeet() + "' " + person.getInches() + "\"", FontFactory.getFont(FontFactory.HELVETICA, 12)));
            pdfptable.addCell(new Phrase(getResources(request).getMessage("label.color.eye") + ": " + getResources(request).getMessage("color.eye." + person.getEyeColor()), FontFactory.getFont(FontFactory.HELVETICA, 12)));
            pdfptable.addCell(new Phrase(getResources(request).getMessage("label.weight") + ": " + person.getWeight() + " " + getResources(request).getMessage("label.weight.lbs"), FontFactory.getFont(FontFactory.HELVETICA, 12)));
            pdfptable.addCell(new Phrase(getResources(request).getMessage("label.race") + ": " + getResources(request).getMessage("race." + person.getRace()), FontFactory.getFont(FontFactory.HELVETICA, 12)));
            document.add(pdfptable);
            redParagraph = new Paragraph(getResources(request).getMessage("label.circumstance"), FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD, new Color(255, 0, 0)));
            redParagraph.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(redParagraph);
            blackParagraph = new Paragraph(person.getCircumstance(), FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL));
            blackParagraph.setAlignment(Paragraph.ALIGN_JUSTIFIED);
            document.add(blackParagraph);
            blackParagraph = new Paragraph("------------------------------------------------------------------------------");
            blackParagraph.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(blackParagraph);
            blackParagraph = new Paragraph(getResources(request).getMessage("global.contact"), FontFactory.getFont(FontFactory.HELVETICA, 12, Font.NORMAL));
            blackParagraph.setAlignment(Paragraph.ALIGN_JUSTIFIED);
            document.add(blackParagraph);
            document.close();
            response.setContentType("application/pdf");
            response.setContentLength(baos.size());
            response.setHeader("Content-disposition", "attachment; filename=Poster.pdf");
            baos.writeTo(response.getOutputStream());
            response.getOutputStream().flush();
            return null;
        } catch (NumberFormatException nfe) {
            return mapping.findForward(Constants.LIST_PERSON);
        } catch (NullPointerException npe) {
            return mapping.findForward(Constants.LIST_PERSON);
        }
    }
