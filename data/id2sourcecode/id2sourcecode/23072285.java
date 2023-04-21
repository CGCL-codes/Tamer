    final String calcApiSig(Param... params) throws ServiceInternalException {
        try {
            digest.reset();
            digest.update(applicationInfo.getSharedSecret().getBytes(ENCODING));
            List<Param> sorted = Arrays.asList(params);
            Collections.sort(sorted);
            for (Param param : sorted) {
                digest.update(param.getName().getBytes(ENCODING));
                digest.update(param.getValue().getBytes(ENCODING));
            }
            return convertToHex(digest.digest());
        } catch (UnsupportedEncodingException e) {
            throw new ServiceInternalException("cannot hahdle properly the encoding", e);
        }
    }
