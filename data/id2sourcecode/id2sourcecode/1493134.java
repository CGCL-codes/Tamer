    private void createCurnit(String name, URL otmlUrl) {
        CreateOtmlModuleParameters params = new CreateOtmlModuleParameters();
        try {
            FileInputStream fis;
            fis = new FileInputStream(new File(otmlUrl.toURI()));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int read;
            while ((read = fis.read(buffer)) != -1) {
                baos.write(buffer, 0, read);
            }
            fis.close();
            params.setOtml(baos.toByteArray());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        params.setName(name);
        params.setUrl(RooloOtmlModuleDao.defaultOtrunkCurnitUrl);
        curnitService.createCurnit(params);
    }
