        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (!(obj instanceof MembershipKey)) {
                return false;
            }
            MembershipKey o = (MembershipKey) obj;
            return getChannel().equals(o.getChannel()) && getGroup().equals(o.getGroup()) && getNetworkInterface().equals(o.getNetworkInterface());
        }
