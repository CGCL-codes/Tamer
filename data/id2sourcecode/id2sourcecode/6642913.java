    public static void main(String[] args) {
        ProtocolManager protocol = null;
        PrintWriter writer = null;
        try {
            if (args.length < 3) {
                System.out.println("Invalid arguments, using default values for [message_template.msg] [template.java.txt] [Packet.java]");
                args = new String[] { "src/libomv/mapgenerator/message_template.msg", "src/libomv/mapgenerator/template.java.txt", "src/libomv/packets/Packet.java" };
            }
            File packets_dir = new File(args[2]).getParentFile();
            protocol = new ProtocolManager(args[0], false);
            writer = WriteHeader(new File(args[2]), args[1]);
            PrintWriter packettype_writer = new PrintWriter(new FileWriter(new File(packets_dir, "PacketType.java")));
            packettype_writer.println("package libomv.packets;\npublic enum PacketType\n{\n    Default,");
            for (int k = 0; k < protocol.LowMaps.mapPackets.size(); k++) {
                MapPacket packet = protocol.LowMaps.mapPackets.elementAt(k);
                if (packet != null) {
                    packettype_writer.println("    " + packet.Name + ",");
                }
            }
            for (int k = 0; k < protocol.MediumMaps.mapPackets.size(); k++) {
                MapPacket packet = protocol.MediumMaps.mapPackets.elementAt(k);
                if (packet != null) {
                    packettype_writer.println("    " + packet.Name + ",");
                }
            }
            for (int k = 0; k < protocol.HighMaps.mapPackets.size(); k++) {
                MapPacket packet = protocol.HighMaps.mapPackets.elementAt(k);
                if (packet != null) {
                    packettype_writer.println("    " + packet.Name + ",");
                }
            }
            packettype_writer.println("}\n");
            packettype_writer.close();
            writer.println("import libomv.StructuredData.OSDMap;\n" + "import libomv.capabilities.CapsMessage.CapsEventType;\n" + "import libomv.types.PacketHeader;\n" + "import libomv.types.PacketFrequency;\n\n" + "public abstract class Packet\n" + "{\n" + "    public static final int MTU = 1200;\n\n" + "    public boolean hasVariableBlocks;\n" + "    public abstract PacketHeader getHeader();\n" + "    public abstract void setHeader(PacketHeader value);\n" + "    public abstract PacketType getType();\n" + "    public abstract int getLength();\n" + "    // Serializes the packet in to a byte array\n" + "    // return A byte array containing the serialized packet payload, ready to be sent across the wire\n" + "    public abstract ByteBuffer ToBytes() throws Exception;\n\n" + "    public ByteBuffer[] ToBytesMultiple()\n    {\n" + "         throw new UnsupportedOperationException(\"ToBytesMultiple()\");\n    }\n" + "    //Get the PacketType for a given packet id and packet frequency\n" + "    //<param name=\"id\">The packet ID from the header</param>\n" + "    //<param name=\"frequency\">Frequency of this packet</param>\n" + "    //<returns>The packet type, or PacketType.Default</returns>\n" + "    public static PacketType getType(short id, byte frequency)\n    {\n" + "        switch (frequency)\n        {\n            case PacketFrequency.Low:\n" + "                switch (id)\n                {");
            for (int k = 0; k < protocol.LowMaps.mapPackets.size(); k++) {
                MapPacket packet = protocol.LowMaps.mapPackets.elementAt(k);
                if (packet != null) {
                    writer.println("                        case (short)" + packet.ID + ": return PacketType." + packet.Name + ";");
                }
            }
            writer.println("                    }\n                    break;\n" + "                case PacketFrequency.Medium:\n                    switch (id)\n                    {");
            for (int k = 0; k < protocol.MediumMaps.mapPackets.size(); k++) {
                MapPacket packet = protocol.MediumMaps.mapPackets.elementAt(k);
                if (packet != null) {
                    writer.println("                        case " + packet.ID + ": return PacketType." + packet.Name + ";");
                }
            }
            writer.println("                    }\n                    break;\n" + "                case PacketFrequency.High:\n                    switch (id)\n" + "                    {");
            for (int k = 0; k < protocol.HighMaps.mapPackets.size(); k++) {
                MapPacket packet = protocol.HighMaps.mapPackets.elementAt(k);
                if (packet != null) {
                    writer.println("                        case " + packet.ID + ": return PacketType." + packet.Name + ";");
                }
            }
            writer.println("                    }\n                    break;\n            }\n\n" + "            return PacketType.Default;\n        }\n");
            writer.println("        /**\n" + "         * Construct a packet in it's native class from a capability OSD structure\n" + "         *\n" + "         * @param bytes Byte array containing the packet, starting at position 0\n" + "         * @param packetEnd The last byte of the packet. If the packet was 76 bytes long, packetEnd would be 75\n" + "         * @returns The native packet class for this type of packet, typecasted to the generic Packet\n" + "         */\n" + "        public static Packet BuildPacket(CapsEventType capsKey,  OSDMap map) throws Exception\n" + "        {\n            return null;\n        }\n\n");
            writer.println("        /**\n" + "         * Construct a packet in it's native class from a byte array\n" + "         *\n" + "         * @param bytes Byte array containing the packet, starting at position 0\n" + "         * @param packetEnd The last byte of the packet. If the packet was 76 bytes long, packetEnd would be 75\n" + "         * @returns The native packet class for this type of packet, typecasted to the generic Packet\n" + "         */\n        public static Packet BuildPacket(ByteBuffer bytes) throws Exception\n" + "        {\n            PacketHeader header = new PacketHeader(bytes);\n" + "            bytes.order(ByteOrder.LITTLE_ENDIAN);\n" + "            bytes.position(header.getLength());\n\n" + "            switch (header.getFrequency())            {\n" + "                case PacketFrequency.Low:\n                    switch (header.getID())\n" + "                    {");
            for (int k = 0; k < protocol.LowMaps.mapPackets.size(); k++) {
                MapPacket packet = protocol.LowMaps.mapPackets.elementAt(k);
                if (packet != null) {
                    writer.println("                        case (short)" + packet.ID + ": return new " + packet.Name + "Packet(header,bytes);");
                }
            }
            writer.println("                    }\n                case PacketFrequency.Medium:\n" + "                    switch (header.getID())\n                    {");
            for (int k = 0; k < protocol.MediumMaps.mapPackets.size(); k++) {
                MapPacket packet = protocol.MediumMaps.mapPackets.elementAt(k);
                if (packet != null) {
                    writer.println("                        case " + packet.ID + ": return new " + packet.Name + "Packet(header, bytes);");
                }
            }
            writer.println("                    }\n                case PacketFrequency.High:\n" + "                    switch (header.getID())\n                    {");
            for (int k = 0; k < protocol.HighMaps.mapPackets.size(); k++) {
                MapPacket packet = protocol.HighMaps.mapPackets.elementAt(k);
                if (packet != null) {
                    writer.println("                        case " + packet.ID + ": return new " + packet.Name + "Packet(header, bytes);");
                }
            }
            writer.println("                    }\n            }\n" + "            throw new Exception(\"Unknown packet ID\");\n        }\n");
            for (int k = 0; k < protocol.LowMaps.mapPackets.size(); k++) {
                MapPacket packet = protocol.LowMaps.mapPackets.elementAt(k);
                if (packet != null) {
                    WritePacketClass(packets_dir, args[1], packet);
                }
            }
            for (int k = 0; k < protocol.MediumMaps.mapPackets.size(); k++) {
                MapPacket packet = protocol.MediumMaps.mapPackets.elementAt(k);
                if (packet != null) {
                    WritePacketClass(packets_dir, args[1], packet);
                }
            }
            for (int k = 0; k < protocol.HighMaps.mapPackets.size(); k++) {
                MapPacket packet = protocol.HighMaps.mapPackets.elementAt(k);
                if (packet != null) {
                    WritePacketClass(packets_dir, args[1], packet);
                }
            }
            writer.println("}");
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
