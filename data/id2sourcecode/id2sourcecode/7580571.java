    @Override
    public synchronized boolean login() throws LoginException {
        if (mCallbackHandler == null) {
            throw new LoginException("No CallbackHandler available");
        }
        boolean authenticated = false;
        mPrincipals.clear();
        try {
            final CredentialsCallback ccb = new CredentialsCallback();
            mCallbackHandler.handle(new Callback[] { ccb });
            final Credentials creds = ccb.getCredentials();
            if (creds != null) {
                if (creds instanceof CARS_Credentials) {
                    UserPrincipal userP = null;
                    final CARS_Credentials sc = (CARS_Credentials) creds;
                    String userName = CARS_Utils.decode(sc.getUserID());
                    final Object attr = sc.getAttribute(SecurityConstants.IMPERSONATOR_ATTRIBUTE);
                    if (attr != null && attr instanceof Subject) {
                    } else {
                        if (!userName.equals(CARS_AccessManager.gSuperuserName)) {
                            final Session loginSession = CARS_Factory.getSystemLoginSession();
                            if (loginSession != null) {
                                synchronized (loginSession) {
                                    if (userName.startsWith(CARS_AccountsApp.AUTHKEY_PREFIX)) {
                                        final String authKey = userName;
                                        userName = getKeyNodeName(authKey);
                                        final Node users = CARS_Factory.getSystemLoginSession().getRootNode().getNode(CARS_AccessManager.gUsersPath);
                                        if ((userName != null) && users.hasNode(userName)) {
                                            final Node user = users.getNode(userName);
                                            if (user.hasProperty(CARS_AccessManager.gPasswordProperty)) {
                                                userP = new UserPrincipal(userName);
                                                authenticated = true;
                                            }
                                        } else {
                                            userName = CARS_AccountsApp.checkCircleOfTrust(sc, authKey.substring(CARS_AccountsApp.AUTHKEY_PREFIX.length()));
                                            if ((userName != null) && users.hasNode(userName)) {
                                                final Node user = users.getNode(userName);
                                                if (user.hasProperty(CARS_AccessManager.gPasswordProperty)) {
                                                    userP = new UserPrincipal(userName);
                                                    authenticated = true;
                                                }
                                            } else {
                                                throw new LoginException("Login error for: " + authKey);
                                            }
                                        }
                                    } else {
                                        final Node users = CARS_Factory.getSystemLoginSession().getRootNode().getNode(CARS_AccessManager.gUsersPath);
                                        if (users.hasNode(userName)) {
                                            final Node user = users.getNode(userName);
                                            if ((user.hasProperty("jecars:Suspended")) && (user.getProperty("jecars:Suspended").getBoolean())) {
                                                throw new AccountLockedException(userName + " is suspended");
                                            }
                                            final String password = new String(sc.getPassword());
                                            if (password.startsWith(JeCARS_RESTServlet.AUTH_TYPE.DIGEST.toString())) {
                                                if ((user.isNodeType("jecars:digestauth") && (user.hasProperty("jecars:HA1")))) {
                                                    final String checkPass = password.substring(JeCARS_RESTServlet.AUTH_TYPE.DIGEST.toString().length() + 1);
                                                    final String[] pass = checkPass.split("\n");
                                                    final String response = pass[0];
                                                    final MessageDigest md = MessageDigest.getInstance("MD5");
                                                    final byte[] md5 = md.digest((user.getProperty("jecars:HA1").getString() + pass[1]).getBytes());
                                                    if (StringUtil.bytesToHexString(md5).equals(response)) {
                                                        authenticated = true;
                                                    }
                                                }
                                            } else {
                                                if (user.hasProperty(CARS_AccessManager.gPasswordProperty)) {
                                                    if (user.getProperty(CARS_AccessManager.gPasswordProperty).getString().equals(CARS_PasswordService.getInstance().encrypt(password))) {
                                                        authenticated = true;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            authenticated = gSuperuserAllowed;
                        }
                    }
                    if (mAnonymousUserId.equals(userName)) {
                        mPrincipals.add(new AnonymousPrincipal());
                    } else {
                        if (userP == null) {
                            mPrincipals.add(new UserPrincipal(userName));
                        } else {
                            mPrincipals.add(userP);
                        }
                    }
                }
            } else if (mDefaultUserId == null) {
                mPrincipals.add(new AnonymousPrincipal());
                authenticated = true;
            } else {
                mPrincipals.add(new UserPrincipal(mDefaultUserId));
                authenticated = true;
            }
        } catch (RepositoryException re) {
            throw new LoginException(re.getMessage());
        } catch (java.io.IOException ioe) {
            throw new LoginException(ioe.toString());
        } catch (UnsupportedCallbackException uce) {
            throw new LoginException(uce.getCallback().toString() + " not available");
        } catch (AccountLockedException ale) {
            throw ale;
        } catch (Exception ne) {
            throw new LoginException(ne.getMessage());
        }
        if (authenticated) {
            return !mPrincipals.isEmpty();
        } else {
            mPrincipals.clear();
            throw new FailedLoginException();
        }
    }
