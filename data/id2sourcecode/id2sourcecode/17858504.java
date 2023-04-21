    @Override
    public StreamMessage recreate(final Session session) throws JMSException {
        if ((this.in != null) || (this.out != null)) throw new javax.jms.IllegalStateException("can not recreate while reading or writing, call reset() first");
        final StreamMessage msg = session.createStreamMessage();
        recreateProperties(session, msg);
        try {
            READ: while (true) {
                final int type = beforeRead();
                switch(type) {
                    case EOF:
                        break READ;
                    case BOOLEAN:
                        msg.writeBoolean(this.in.readBoolean());
                        break;
                    case BYTE:
                        msg.writeByte(this.in.readByte());
                        break;
                    case CHAR:
                        msg.writeChar(this.in.readChar());
                        break;
                    case DOUBLE:
                        msg.writeDouble(this.in.readDouble());
                        break;
                    case FLOAT:
                        msg.writeFloat(this.in.readFloat());
                        break;
                    case INT:
                        msg.writeInt(this.in.readInt());
                        break;
                    case LONG:
                        msg.writeLong(this.in.readLong());
                        break;
                    case SHORT:
                        msg.writeShort(this.in.readShort());
                        break;
                    case STRING:
                        msg.writeString(readString());
                        break;
                    case NULL:
                        msg.writeString(null);
                        break;
                    case ARRAY:
                        {
                            final byte[] a = new byte[this.inArrayRemaining];
                            this.in.readFully(a);
                            this.inArrayRemaining = 0;
                            msg.writeBytes(a);
                        }
                        break;
                    default:
                        throw new AssertionError(type);
                }
                this.inType = 0;
            }
            msg.reset();
            return msg;
        } catch (final IOException ex) {
            throw exceptionOnRead(ex);
        } finally {
            reset();
        }
    }
