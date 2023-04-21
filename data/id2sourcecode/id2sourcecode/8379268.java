    @Override
    @Transactional(readOnly = true)
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        authentication.setAuthenticated(false);
        User user = dao.loadByQuery("from User where username=:username", Collections.singletonMap("username", authentication.getName()));
        if (user != null && user.getPassword() != null && ((digester == null && user.getPassword().equals(authentication.getCredentials())) || (digester != null && MessageDigest.isEqual(user.getPassword().getBytes(), digester.digest(((String) authentication.getCredentials()).getBytes()))))) {
            return new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
        }
        return null;
    }
