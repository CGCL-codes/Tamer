    protected File asFile(WebRequest request) {
        digest.reset();
        digest.update(request.getUrl().toString().getBytes());
        BigInteger i = new BigInteger(1, digest.digest());
        String name = String.valueOf(i).replace(",", "");
        File result = new File(cacheDirectory, name);
        result.getParentFile().mkdirs();
        return result;
    }
