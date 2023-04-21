    public boolean _updateUser(NSDictionary userInfo) {
        String name = (String) userInfo.valueForKey("username");
        if ((name == null) || (name.equals(""))) {
            System.out.println("SimpleAuthentication._updateUser() - NO USERNAME");
            return false;
        }
        if (!_checkUserExists(name)) {
            System.out.println("SimpleAuthentication._updateUser(): " + name + " doesn't exist!\n");
            return false;
        }
        try {
            _ec.lock();
            _md.reset();
            NSMutableDictionary matchingDict = new NSMutableDictionary();
            matchingDict.takeValueForKey(name, "username");
            matchingDict.takeValueForKey(_realm, "realm");
            EOEnterpriseObject eo = EOUtilities.objectMatchingValues(_ec, _authRecordEntityName, matchingDict.immutableClone());
            System.out.println("attempting to update EO: " + eo.snapshot());
            String pass = (String) userInfo.valueForKey("password");
            String tempPass = (String) userInfo.valueForKey("temppassword");
            if (pass != null) {
                try {
                    _md.update(pass.getBytes("UTF-8"));
                } catch (UnsupportedEncodingException uee) {
                    _ec.unlock();
                    System.out.println("SimpleAuthentication._updateUser() - UNSUPPORTEDENCODINGEXCEPTION");
                    return false;
                }
                byte[] md5 = _md.digest();
                NSData passHash = new NSData(md5);
                eo.takeStoredValueForKey(passHash, "password");
                eo.takeStoredValueForKey(null, "temppassword");
            } else if (tempPass != null) {
                try {
                    _md.update(tempPass.getBytes("UTF-8"));
                } catch (UnsupportedEncodingException uee) {
                    _ec.unlock();
                    System.out.println("SimpleAuthentication._updateUser() - UNSUPPORTED ENCODINGEXCEPTION");
                    return false;
                }
                byte[] md5 = _md.digest();
                NSData tempPassHash = new NSData(md5);
                eo.takeStoredValueForKey(tempPassHash, "temppassword");
            }
            _ec.saveChanges();
            _ec.unlock();
            return true;
        } catch (Exception e) {
            _ec.unlock();
            System.out.println("SimpleAuthentication._updateUser() - EXCEPTION SAVING USER RECORD");
            e.printStackTrace();
            return false;
        }
    }
