    public String getOperations() {
        String str = new String("    // ----------------------- Operations -----------------\n");
        Iterator<MemberDef> iterator = members.iterator();
        while (iterator.hasNext()) {
            MemberDef temp = iterator.next();
            if (temp.Name.equals("Span") || temp.Name.equals("SpanA")) {
                hasSpan = true;
            }
            if (temp.Name.equals("Channel") || temp.Name.equals("ChannelA")) {
                hasChannel = true;
            }
            if (temp.Name.equals("SpanB")) {
                hasSpanB = true;
            }
            if (temp.Name.equals("ChannelB")) {
                hasChannelB = true;
            }
            if (temp.Name.equals("AddrInfo")) {
                hasAIB = true;
            }
            str += temp.getOperations();
        }
        if (hasAIB) {
            str += "    // Is this message a one channel message\n";
            str += "    public boolean isOneChannelMessage() {\n" + "        return ((m_AddrInfo[0] == 0x00) && (m_AddrInfo[1] == 0x01) &&\n" + "            (m_AddrInfo[2] == 0x0d) && (m_AddrInfo[3] == 0x03));\n" + "    }\n\n";
            str += "    // Is this message a two channel message\n";
            str += "    public boolean isTwoChannelMessage() {\n" + "        return ((m_AddrInfo[0] == 0x00) && (m_AddrInfo[1] == 0x02) &&\n" + "            (m_AddrInfo[2] == 0x0d) && (m_AddrInfo[3] == 0x03) &&\n" + "            (m_AddrInfo[7] == 0x0d) && (m_AddrInfo[8] == 0x03));\n" + "    }\n\n";
            str += "    // Is this a channel related message at all?\n" + "    public boolean isChannelRelatedMessage() {\n" + "        return isOneChannelMessage() || isTwoChannelMessage();\n" + "    }\n\n";
            if (name.equals("XL_PPLEventIndication") || name.equals("XL_PPLEventRequest") || name.equals("XL_DS0StatusChange") || name.equals("XL_PlayFileStart") || name.equals("XL_PlayFileStop") || name.equals("XL_RecordFileStart") || name.equals("XL_RecordFileStop") || name.equals("XL_CallProcessingEvent")) {
                str += "    public int getSpan() {\n" + "        if (isChannelRelatedMessage()) {\n" + "            return (m_AddrInfo[4] << 8) | m_AddrInfo[5];\n" + "        } else {\n" + "            return -1;\n" + "        }\n" + "    };\n";
                str += "    public byte getChannel() {\n" + "        if (isChannelRelatedMessage()) {\n" + "            return m_AddrInfo[6];\n" + "        } else {\n" + "            return -1;\n" + "        }\n" + "    };\n";
                str += "    public int getSpanA() {\n" + "        if (isTwoChannelMessage()) {\n" + "            return (m_AddrInfo[4] << 8) | m_AddrInfo[5];\n" + "        } else {\n" + "            return -1;\n" + "        }\n" + "    };\n";
                str += "    public byte getChannelA() {\n" + "        if (isTwoChannelMessage()) {\n" + "            return m_AddrInfo[6];\n" + "        } else {\n" + "            return -1;\n" + "        }\n" + "    };\n";
                str += "    public int getSpanB() {\n" + "        if (isTwoChannelMessage()) {\n" + "            return (m_AddrInfo[9] << 8) | m_AddrInfo[10];\n" + "        } else {\n" + "            return -1;\n" + "        }\n" + "    };\n";
                str += "    public byte getChannelB() {\n" + "        if (isTwoChannelMessage()) {\n" + "            return m_AddrInfo[11];\n" + "        } else {\n" + "            return -1;\n" + "        }\n" + "    };\n";
                str += "    public void setSpanChannel(int Span, byte Channel) {\n" + "        m_AddrInfo[0] = 0x00; \n" + "        m_AddrInfo[1] = 0x01; \n" + "        m_AddrInfo[2] = 0x0D; \n" + "        m_AddrInfo[3] = 0x03; \n" + "        m_AddrInfo[4] = (byte)((Span >> 8) & 0xFF); \n" + "        m_AddrInfo[5] = (byte)(Span & 0xFF); \n" + "        m_AddrInfo[6] = (byte)(Channel & 0xFF); \n" + "    };\n";
                str += "    public void setSpanChannels(int SpanA, byte ChannelA, int SpanB, byte ChannelB) {\n" + "        m_AddrInfo[0] = 0x00; \n" + "        m_AddrInfo[1] = 0x02; \n" + "        m_AddrInfo[2] = 0x0D; \n" + "        m_AddrInfo[3] = 0x03; \n" + "        m_AddrInfo[4] = (byte)((SpanA >> 8) & 0xFF); \n" + "        m_AddrInfo[5] = (byte)(SpanA & 0xFF); \n" + "        m_AddrInfo[6] = (byte)(ChannelA & 0xFF); \n" + "        m_AddrInfo[7] = 0x0D; \n" + "        m_AddrInfo[8] = 0x03; \n" + "        m_AddrInfo[9] = (byte)((SpanB >> 8) & 0xFF); \n" + "        m_AddrInfo[10] = (byte)(SpanB & 0xFF); \n" + "        m_AddrInfo[11] = (byte)(ChannelB & 0xFF); \n" + "    };\n";
            }
        } else {
            if (hasSpan && hasChannel) {
                str += "    // Is this message a channel related message?\n";
                str += "    public boolean isChannelRelatedMessage() { return true; };\n\n";
            }
            if (hasSpanB && hasChannelB) {
                str += "    // Is this message a two channel message\n";
                str += "    public boolean isTwoChannelMessage() { return true; };\n\n";
                str += "    // Is this message a one channel message\n";
                str += "    public boolean isOneChannelMessage() { return false; };\n";
            } else if (hasSpan && hasChannel) {
                str += "    // Is this message a two channel message\n";
                str += "    public boolean isTwoChannelMessage() { return false; };\n\n";
                str += "    // Is this message a one channel message\n";
                str += "    public boolean isOneChannelMessage() { return true; };\n\n";
            }
        }
        return str;
    }
