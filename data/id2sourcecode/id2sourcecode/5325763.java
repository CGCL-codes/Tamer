    @Override
    public void onHTMLInputFileElement(HTMLInputElement element) {
        Element fileElem = handleHTMLInputElement(element);
        fileElem.setAttribute("name", element.getName());
        String filename = element.getValue();
        if (filename == null || filename.trim().length() == 0) {
            logger.debug("File input element is empty; element.name=" + element.getName());
            return;
        }
        try {
            File file = new File(filename);
            fileElem.setAttribute("filename", file.getName());
            FileInputStream fis = new FileInputStream(file);
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            byte[] buffer = new byte[1024];
            int count = 0;
            while ((count = fis.read(buffer)) != -1) {
                digest.update(buffer, 0, count);
            }
            fis.close();
            byte[] digestBytes = digest.digest();
            fileElem.setAttribute("value", HexStringHelper.toHexString(digestBytes));
            document.getDocumentElement().appendChild(fileElem);
        } catch (FileNotFoundException e) {
            logger.warn("File not found: " + filename, e);
        } catch (IOException e) {
            logger.warn("I/O error: " + filename, e);
        } catch (NoSuchAlgorithmException e) {
            logger.warn("No such algorithm exception: " + e);
        }
    }
