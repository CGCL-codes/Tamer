    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mType);
        dest.writeString(mTo);
        dest.writeString(mBody);
        dest.writeString(mSubject);
        dest.writeString(mThread);
        dest.writeString(mFrom);
        dest.writeLong(mTimestamp.getTime());
    }
