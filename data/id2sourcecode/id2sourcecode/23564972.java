            public void writeTo(com.google.protobuf.CodedOutputStream output) throws java.io.IOException {
                getSerializedSize();
                if (hasTitle()) {
                    output.writeString(1, getTitle());
                }
                if (hasSnippet()) {
                    output.writeString(2, getSnippet());
                }
                if (hasWaveId()) {
                    output.writeString(3, getWaveId());
                }
                if (hasLastModified()) {
                    output.writeInt64(4, getLastModified());
                }
                if (hasUnreadCount()) {
                    output.writeInt32(5, getUnreadCount());
                }
                if (hasBlipCount()) {
                    output.writeInt32(6, getBlipCount());
                }
                for (java.lang.String element : getParticipantsList()) {
                    output.writeString(7, element);
                }
                if (hasAuthor()) {
                    output.writeString(8, getAuthor());
                }
                getUnknownFields().writeTo(output);
            }
