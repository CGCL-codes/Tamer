    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String uploadPhoto(@RequestParam("data") MultipartFile file, HttpServletRequest request, Map model) {
        Person oldPerson = personService.findPersonByEmail(getUserEmail());
        File outputFile = new File("src/main/webapp/resources/img/avatars/" + oldPerson.getPersonId() + ".jpg");
        if (file != null) {
            try {
                if (file.getContentType().equals("image/jpeg")) {
                    InputStream in = file.getInputStream();
                    FileOutputStream out = new FileOutputStream(outputFile);
                    if (outputFile.exists()) {
                        outputFile.delete();
                        outputFile.createNewFile();
                    } else {
                        outputFile.createNewFile();
                    }
                    while (in.available() > 0) {
                        out.write(in.read());
                    }
                    oldPerson.setAvatar(String.valueOf(oldPerson.getPersonId()));
                    personService.updatePerson(oldPerson);
                    in.close();
                    out.close();
                } else {
                    request.setAttribute("msg", "You chose incorrect image");
                    request.setAttribute("person", oldPerson);
                    putFacultiesAndUniversities(model);
                    return "edit";
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
        request.setAttribute("msg", "Your avatar has been changed");
        request.setAttribute("person", oldPerson);
        putFacultiesAndUniversities(model);
        return "edit";
    }
