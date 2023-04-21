    @Override
    public void fillInputData(InputData inputData) throws TransferFailedException, ResourceDoesNotExistException, AuthorizationException {
        InputStream is;
        Resource resource = inputData.getResource();
        try {
            String name = resource.getName();
            String url = buildMvnUrl(resource);
            if (name.endsWith(".sha1") || name.contains("maven-metadata")) {
                is = new ByteArrayInputStream(new byte[0]);
            } else {
                is = new URL(url).openStream();
                if (is == null) throw new ResourceDoesNotExistException(resource.getName());
            }
        } catch (IOException e) {
            throw new ResourceDoesNotExistException(resource.getName());
        } catch (Exception e) {
            throw new TransferFailedException(e.getMessage(), e);
        }
        inputData.setInputStream(is);
    }
