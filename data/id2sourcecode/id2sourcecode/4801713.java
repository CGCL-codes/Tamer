    @Override
    public void writeToParcel(Parcel out, int parcelableFlags) {
        super.writeToParcel(out, parcelableFlags);
        out.writeString(authority);
        out.writeString(readPermission);
        out.writeString(writePermission);
        out.writeInt(grantUriPermissions ? 1 : 0);
        out.writeTypedArray(uriPermissionPatterns, parcelableFlags);
        out.writeTypedArray(pathPermissions, parcelableFlags);
        out.writeInt(multiprocess ? 1 : 0);
        out.writeInt(initOrder);
        out.writeInt(isSyncable ? 1 : 0);
    }
