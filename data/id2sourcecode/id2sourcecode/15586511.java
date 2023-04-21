    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer("Group ");
        sb.append(friendlyName());
        sb.append(": public key: ");
        if (!_groupPublicKey.available()) {
            sb.append("not ready, write to " + GroupAccessControlProfile.groupPublicKeyName(_groupNamespace, friendlyName()));
        } else {
            sb.append(publicKeyName());
        }
        sb.append(" membership list: ");
        if ((null == _groupMembers) || (!_groupMembers.available())) {
            sb.append("not ready, will write to " + GroupAccessControlProfile.groupMembershipListName(_groupNamespace, friendlyName()));
        } else {
            try {
                sb.append(membershipListName());
            } catch (Exception e) {
                if (Log.isLoggable(Log.FAC_ACCESSCONTROL, Level.WARNING)) {
                    Log.warning(Log.FAC_ACCESSCONTROL, "Unexpected " + e.getClass().getName() + " exception in getMembershipListName(): " + e.getMessage());
                }
                sb.append("Membership list name unavailable!");
            }
        }
        return sb.toString();
    }
