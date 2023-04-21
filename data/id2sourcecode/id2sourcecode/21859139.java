    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(this.id);
        out.writeString(this.login);
        out.writeString(this.status);
        out.writeInt(this.unread);
        out.writeTypedList(this.MessageList);
    }
