    protected String format(LocoNetMessage l) {
        boolean showStatus = false;
        int minutes;
        int hours;
        int frac_mins;
        switch(l.getOpCode()) {
            case LnConstants.OPC_IDLE:
                {
                    return "Force Idle, Broadcast Emergency STOP.\n";
                }
            case LnConstants.OPC_GPON:
                {
                    return "Global Power ON.\n";
                }
            case LnConstants.OPC_GPOFF:
                {
                    return "Global Power OFF.\n";
                }
            case LnConstants.OPC_GPBUSY:
                {
                    return "Master is busy.\n";
                }
            case LnConstants.OPC_LOCO_ADR:
                {
                    int adrHi = l.getElement(1);
                    int adrLo = l.getElement(2);
                    return "Request slot for loco address " + convertToMixed(adrLo, adrHi) + ".\n";
                }
            case LnConstants.OPC_SW_ACK:
                {
                    int sw2 = l.getElement(2);
                    String turnoutSystemName = "";
                    String turnoutUserName = "";
                    turnoutSystemName = locoNetTurnoutPrefix + SENSOR_ADR(l.getElement(1), l.getElement(2));
                    try {
                        jmri.Turnout turnout = turnoutManager.getBySystemName(turnoutSystemName);
                        if ((turnout != null) && (turnout.getUserName().length() > 0)) turnoutUserName = "(" + turnout.getUserName() + ")"; else turnoutUserName = "()";
                    } catch (Exception e) {
                        turnoutUserName = "()";
                    }
                    return "Request switch " + turnoutSystemName + " " + turnoutUserName + ((sw2 & LnConstants.OPC_SW_ACK_CLOSED) != 0 ? " Closed/Green" : " Thrown/Red") + ((sw2 & LnConstants.OPC_SW_ACK_OUTPUT) != 0 ? " (Output On)" : " (Output Off)") + " with acknowledgement.\n";
                }
            case LnConstants.OPC_SW_STATE:
                {
                    String turnoutSystemName = "";
                    String turnoutUserName = "";
                    turnoutSystemName = locoNetTurnoutPrefix + SENSOR_ADR(l.getElement(1), l.getElement(2));
                    try {
                        jmri.Turnout turnout = turnoutManager.getBySystemName(turnoutSystemName);
                        if ((turnout != null) && (turnout.getUserName().length() > 0)) turnoutUserName = "(" + turnout.getUserName() + ")"; else turnoutUserName = "()";
                    } catch (Exception e) {
                        turnoutUserName = "()";
                    }
                    return "Request status of switch " + turnoutSystemName + " " + turnoutUserName + ".\n";
                }
            case LnConstants.OPC_RQ_SL_DATA:
                {
                    int slot = l.getElement(1);
                    switch(slot) {
                        case LnConstants.FC_SLOT:
                            return "Request Fast Clock information.\n";
                        case LnConstants.CFG_SLOT:
                            return "Request Command Station Ops Switches.\n";
                        case LnConstants.PRG_SLOT:
                            return "Request Programming Track information.\n";
                        default:
                            return "Request data/status for slot " + slot + ".\n";
                    }
                }
            case LnConstants.OPC_MOVE_SLOTS:
                {
                    int src = l.getElement(1);
                    int dest = l.getElement(2);
                    if (src == 0) {
                        return "Get most recently dispatched slot.\n";
                    } else if (src == dest) {
                        return "Set status of slot " + src + " to IN_USE.\n";
                    } else if (dest == 0) {
                        return "Mark slot " + src + " as DISPATCHED.\n";
                    } else {
                        return "Move data in slot " + src + " to slot " + dest + ".\n";
                    }
                }
            case LnConstants.OPC_LINK_SLOTS:
                {
                    int src = l.getElement(1);
                    int dest = l.getElement(2);
                    return "Consist loco in slot " + src + " to loco in slot " + dest + ".\n";
                }
            case LnConstants.OPC_UNLINK_SLOTS:
                {
                    int src = l.getElement(1);
                    int dest = l.getElement(2);
                    return "Remove loco in slot " + src + " from consist with loco in slot " + dest + ".\n";
                }
            case LnConstants.OPC_CONSIST_FUNC:
                {
                    int slot = l.getElement(1);
                    int dirf = l.getElement(2);
                    return "Set consist in slot " + slot + " direction to " + ((dirf & LnConstants.DIRF_DIR) != 0 ? "REV" : "FWD") + "F0=" + ((dirf & LnConstants.DIRF_F0) != 0 ? "On, " : "Off,") + "F1=" + ((dirf & LnConstants.DIRF_F1) != 0 ? "On, " : "Off,") + "F2=" + ((dirf & LnConstants.DIRF_F2) != 0 ? "On, " : "Off,") + "F3=" + ((dirf & LnConstants.DIRF_F3) != 0 ? "On, " : "Off,") + "F4=" + ((dirf & LnConstants.DIRF_F4) != 0 ? "On" : "Off") + ".\n";
                }
            case LnConstants.OPC_SLOT_STAT1:
                {
                    int slot = l.getElement(1);
                    int stat = l.getElement(2);
                    return "Write slot " + slot + " with status value " + stat + " (0x" + Integer.toHexString(stat) + ") - Loco is " + LnConstants.CONSIST_STAT(stat) + ", " + LnConstants.LOCO_STAT(stat) + "\n\tand operating in " + LnConstants.DEC_MODE(stat) + " speed step mode.\n";
                }
            case LnConstants.OPC_LONG_ACK:
                {
                    int opcode = l.getElement(1);
                    int ack1 = l.getElement(2);
                    switch(opcode | 0x80) {
                        case (LnConstants.OPC_LOCO_ADR):
                            return "LONG_ACK: NO FREE SLOTS!\n";
                        case (LnConstants.OPC_LINK_SLOTS):
                            return "LONG_ACK: Invalid consist, unable to link.\n";
                        case (LnConstants.OPC_SW_ACK):
                            if (ack1 == 0) {
                                return "LONG_ACK: The Command Station FIFO is full, the switch command was rejected.\n";
                            } else if (ack1 == 0x7f) {
                                return "LONG_ACK: The Command Station accepted the switch command.\n";
                            } else {
                                forceHex = true;
                                return "LONG_ACK: Unknown response to 'Request Switch with ACK' command, value 0x" + Integer.toHexString(ack1) + ".\n";
                            }
                        case (LnConstants.OPC_SW_REQ):
                            return "LONG_ACK: Switch request Failed!\n";
                        case (LnConstants.OPC_WR_SL_DATA):
                            if (ack1 == 0) {
                                return "LONG_ACK: The Slot Write command was rejected.\n";
                            } else if (ack1 == 0x01) {
                                return "LONG_ACK: The Slot Write command was accepted.\n";
                            } else if (ack1 == 0x23 || ack1 == 0x2b || ack1 == 0x6B) {
                                return "LONG_ACK: DCS51 programming reply, thought to mean OK.\n";
                            } else if (ack1 == 0x40) {
                                return "LONG_ACK: The Slot Write command was accepted blind (no response will be sent).\n";
                            } else if (ack1 == 0x7f) {
                                return "LONG_ACK: Function not implemented, no reply will follow.\n";
                            } else {
                                forceHex = true;
                                return "LONG_ACK: Unknown response to Write Slot Data message value 0x" + Integer.toHexString(ack1) + ".\n";
                            }
                        case (LnConstants.OPC_SW_STATE):
                            return "LONG_ACK: Command station response to switch state request 0x" + Integer.toHexString(ack1) + (((ack1 & 0x20) != 0) ? " (Closed)" : " (Thrown)") + ".\n";
                        case (LnConstants.OPC_MOVE_SLOTS):
                            if (ack1 == 0) {
                                return "LONG_ACK: The Move Slots command was rejected.\n";
                            } else if (ack1 == 0x7f) {
                                return "LONG_ACK: The Move Slots command was accepted.\n";
                            } else {
                                forceHex = true;
                                return "LONG_ACK: unknown reponse to Move Slots message 0x" + Integer.toHexString(ack1) + ".\n";
                            }
                        case LnConstants.OPC_IMM_PACKET:
                            if (ack1 == 0) {
                                return "LONG_ACK: the Send IMM Packet command was rejected, the buffer is full/busy.\n";
                            } else if (ack1 == 0x7f) {
                                return "LONG_ACK: the Send IMM Packet command was accepted.\n";
                            } else {
                                forceHex = true;
                                return "LONG_ACK: Unknown reponse to Send IMM Packet value 0x" + Integer.toHexString(ack1) + ".\n";
                            }
                        case LnConstants.OPC_IMM_PACKET_2:
                            return "LONG_ACK: the Lim Master responded to the Send IMM Packet command with " + ack1 + " (0x" + Integer.toHexString(ack1) + ").\n";
                        case LnConstants.RE_LACK_SPEC_CASE1:
                        case LnConstants.RE_LACK_SPEC_CASE2:
                            int responseValue = l.getElement(2) & 0x20;
                            String state = (responseValue == 0x20) ? "1 (Closed).\n" : "0 (Thrown).\n";
                            return "LONG_ACK: OpSwitch report - opSwitch is " + state;
                        default:
                            return "LONG_ACK: Response " + ack1 + " (0x" + Integer.toHexString(ack1) + ") to opcode 0x" + Integer.toHexString(opcode) + " not decoded.\n";
                    }
                }
            case LnConstants.OPC_INPUT_REP:
                {
                    int in1 = l.getElement(1);
                    int in2 = l.getElement(2);
                    int contactNum = ((SENSOR_ADR(in1, in2) - 1) * 2 + ((in2 & LnConstants.OPC_INPUT_REP_SW) != 0 ? 2 : 1));
                    String sensorSystemName = locoNetSensorPrefix + contactNum;
                    String sensorUserName = "";
                    try {
                        sensorSystemName = locoNetSensorPrefix + contactNum;
                        jmri.Sensor sensor = sensorManager.getBySystemName(sensorSystemName);
                        if ((sensor != null) && (sensor.getUserName().length() > 0)) {
                            sensorUserName = " (" + sensor.getUserName() + ")";
                        } else {
                            sensorUserName = "()";
                        }
                    } catch (Exception e) {
                        sensorUserName = "()";
                    }
                    int sensor = (SENSOR_ADR(in1, in2) - 1) * 2 + ((in2 & LnConstants.OPC_INPUT_REP_SW) != 0 ? 2 : 1);
                    int bdlid = ((sensor - 1) / 16) + 1;
                    int bdlin = ((sensor - 1) % 16) + 1;
                    String bdl = "BDL16 #" + bdlid + ", DS" + bdlin;
                    int boardid = ((sensor - 1) / 8) + 1;
                    int boardindex = ((sensor - 1) % 8);
                    String ds54sensors[] = { "AuxA", "SwiA", "AuxB", "SwiB", "AuxC", "SwiC", "AuxD", "SwiD" };
                    String ds64sensors[] = { "A1", "S1", "A2", "S2", "A3", "S3", "A4", "S4" };
                    String se8csensors[] = { "DS01", "DS02", "DS03", "DS04", "DS05", "DS06", "DS07", "DS08" };
                    return "Sensor " + sensorSystemName + " " + sensorUserName + " is " + ((in2 & LnConstants.OPC_INPUT_REP_HI) != 0 ? "Hi" : "Lo") + ".  (" + bdl + "; DS54/64" + (sensor < 289 ? "/SE8c #" : " #") + boardid + ", " + ds54sensors[boardindex] + "/" + ds64sensors[boardindex] + ((sensor < 289) ? "/" + se8csensors[boardindex] : "") + ")\n";
                }
            case LnConstants.OPC_SW_REP:
                {
                    int sn1 = l.getElement(1);
                    int sn2 = l.getElement(2);
                    String turnoutSystemName = "";
                    String turnoutUserName = "";
                    turnoutSystemName = locoNetTurnoutPrefix + SENSOR_ADR(sn1, sn2);
                    try {
                        jmri.Turnout turnout = turnoutManager.getBySystemName(turnoutSystemName);
                        if ((turnout != null) && (turnout.getUserName().length() > 0)) turnoutUserName = "(" + turnout.getUserName() + ")"; else turnoutUserName = "()";
                    } catch (Exception e) {
                        turnoutUserName = "()";
                    }
                    if ((sn2 & LnConstants.OPC_SW_REP_INPUTS) != 0) {
                        return "Turnout " + turnoutSystemName + " " + turnoutUserName + " " + ((sn2 & LnConstants.OPC_SW_REP_SW) != 0 ? " Switch input" : " Aux input") + " is " + (((sn2 & LnConstants.OPC_SW_REP_HI) != 0) ? "Closed (input off)" : "Thrown (input on)") + ".\n";
                    } else {
                        return "Turnout " + turnoutSystemName + " " + turnoutUserName + " " + " output state: Closed output is " + ((sn2 & LnConstants.OPC_SW_REP_CLOSED) != 0 ? "ON (sink)" : "OFF (open)") + ", Thrown output is " + ((sn2 & LnConstants.OPC_SW_REP_THROWN) != 0 ? "ON (sink)" : "OFF (open)") + ".\n";
                    }
                }
            case LnConstants.OPC_SW_REQ:
                {
                    int sw1 = l.getElement(1);
                    int sw2 = l.getElement(2);
                    String retVal;
                    int a = (sw2 & 0x20) >> 5;
                    int c = (sw1 & 0x02) >> 1;
                    int b = (sw1 & 0x01);
                    int topbits = 0;
                    int midbits = (a << 2) + (c << 1) + b;
                    int count = 0;
                    StringBuilder addrListB = new StringBuilder();
                    for (topbits = 0; topbits < 32; topbits++) {
                        int lval = (topbits << 6) + (midbits << 3) + 1;
                        int hval = lval + 7;
                        if ((count % 8) > 0) {
                            addrListB.append(", ");
                        } else {
                            if (count == 0) {
                                addrListB.append("\t");
                            } else {
                                addrListB.append(",\n\t");
                            }
                        }
                        addrListB.append("" + lval);
                        addrListB.append("-" + hval);
                        count++;
                    }
                    addrListB.append("\n");
                    String addrList = new String(addrListB);
                    if (((sw2 & 0xCF) == 0x0F) && ((sw1 & 0xFC) == 0x78)) {
                        retVal = "Interrogate Stationary Decoders with bits a/c/b of " + a + "/" + c + "/" + b + "; turnouts...\n" + addrList;
                    } else if (((sw2 & 0xCF) == 0x07) && ((sw1 & 0xFC) == 0x78)) {
                        retVal = "Interrogate LocoNet Turnouts/Sensors with bits a/c/b of " + a + "/" + c + "/" + b + "; addresses...\n" + addrList;
                    } else {
                        String turnoutSystemName = "";
                        String turnoutUserName = "";
                        turnoutSystemName = locoNetTurnoutPrefix + SENSOR_ADR(l.getElement(1), l.getElement(2));
                        try {
                            jmri.Turnout turnout = turnoutManager.getBySystemName(turnoutSystemName);
                            if ((turnout != null) && (turnout.getUserName().length() > 0)) turnoutUserName = "(" + turnout.getUserName() + ")"; else turnoutUserName = "()";
                        } catch (Exception e) {
                            turnoutUserName = "()";
                        }
                        retVal = "Requesting Switch at " + turnoutSystemName + " " + turnoutUserName + " to " + ((sw2 & LnConstants.OPC_SW_REQ_DIR) != 0 ? "Closed" : "Thrown") + " (output " + ((sw2 & LnConstants.OPC_SW_REQ_OUT) != 0 ? "On" : "Off") + ").\n";
                    }
                    return retVal;
                }
            case LnConstants.OPC_LOCO_SND:
                {
                    int slot = l.getElement(1);
                    int snd = l.getElement(2);
                    return "Set loco in slot " + slot + " Sound1/F5=" + ((snd & LnConstants.SND_F5) != 0 ? "On" : "Off") + ", Sound2/F6=" + ((snd & LnConstants.SND_F6) != 0 ? "On" : "Off") + ", Sound3/F7=" + ((snd & LnConstants.SND_F7) != 0 ? "On" : "Off") + ", Sound4/F8=" + ((snd & LnConstants.SND_F8) != 0 ? "On" : "Off") + ".\n";
                }
            case LnConstants.OPC_LOCO_DIRF:
                {
                    int slot = l.getElement(1);
                    int dirf = l.getElement(2);
                    return "Set loco in slot " + slot + " direction to " + ((dirf & LnConstants.DIRF_DIR) != 0 ? "REV" : "FWD") + ", F0=" + ((dirf & LnConstants.DIRF_F0) != 0 ? "On, " : "Off,") + " F1=" + ((dirf & LnConstants.DIRF_F1) != 0 ? "On, " : "Off,") + " F2=" + ((dirf & LnConstants.DIRF_F2) != 0 ? "On, " : "Off,") + " F3=" + ((dirf & LnConstants.DIRF_F3) != 0 ? "On, " : "Off,") + " F4=" + ((dirf & LnConstants.DIRF_F4) != 0 ? "On" : "Off") + ".\n";
                }
            case LnConstants.OPC_LOCO_SPD:
                {
                    int slot = l.getElement(1);
                    int spd = l.getElement(2);
                    if (spd == LnConstants.OPC_LOCO_SPD_ESTOP) {
                        return "Set speed of loco in slot " + slot + " to EMERGENCY STOP!\n";
                    } else {
                        return "Set speed of loco in slot " + slot + " to " + spd + ".\n";
                    }
                }
            case LnConstants.OPC_PANEL_QUERY:
                {
                    switch(l.getElement(1)) {
                        case 0x00:
                            {
                                return "Query Tetherless Receivers.\n";
                            }
                        case 0x40:
                            {
                                if (l.getElement(2) == 0x1F) {
                                    return "Set LocoNet ID to " + l.getElement(3) + ".\n";
                                } else {
                                    return "Unknown attempt to set the Loconet ID 0x" + Integer.toHexString(l.getElement(2)) + ".\n";
                                }
                            }
                        default:
                            {
                                return "Unknown Tetherless Receivers Request 0x" + Integer.toHexString(l.getElement(1)) + ".\n";
                            }
                    }
                }
            case LnConstants.OPC_PANEL_RESPONSE:
                {
                    switch(l.getElement(1)) {
                        case 0x12:
                            {
                                return "UR-92 Responding with LocoNet ID " + (l.getElement(3) & 0x07) + ((l.getElement(3) & 0x08) == 0x08 ? ", duplex enabled.\n" : ".\n");
                            }
                        case 0x17:
                            {
                                return "UR-90 Responding with LocoNet ID " + l.getElement(3) + ".\n";
                            }
                        case 0x1F:
                            {
                                return "UR-91 Responding with LocoNet ID " + l.getElement(3) + ".\n";
                            }
                        default:
                            {
                                return "Unknown Tetherless Receiver of type 0x" + Integer.toHexString(l.getElement(1)) + " responding.\n";
                            }
                    }
                }
            case LnConstants.OPC_MULTI_SENSE:
                {
                    int type = l.getElement(1) & LnConstants.OPC_MULTI_SENSE_MSG;
                    int section = (l.getElement(2) / 16) + (l.getElement(1) & 0x1F) * 8;
                    switch(type) {
                        case LnConstants.OPC_MULTI_SENSE_POWER:
                            int pCMD = (l.getElement(3) & 0xF0);
                            if ((pCMD == 0x30) || (pCMD == 0x10)) {
                                int cm1 = l.getElement(3);
                                int cm2 = l.getElement(4);
                                return "PM4 " + (l.getElement(2) + 1) + " ch1 " + ((cm1 & 1) != 0 ? "AR " : "SC ") + ((cm2 & 1) != 0 ? "ACT;" : "OK;") + " ch2 " + ((cm1 & 2) != 0 ? "AR " : "SC ") + ((cm2 & 2) != 0 ? "ACT;" : "OK;") + " ch3 " + ((cm1 & 4) != 0 ? "AR " : "SC ") + ((cm2 & 4) != 0 ? "ACT;" : "OK;") + " ch4 " + ((cm1 & 8) != 0 ? "AR " : "SC ") + ((cm2 & 8) != 0 ? "ACT;" : "OK;") + "\n";
                            } else if (pCMD == 0x70) {
                                int deviceType = l.getElement(3) & 0x7;
                                String device;
                                if (deviceType == 0) device = "PM4(x) "; else if (deviceType == 1) device = "BDL16(x) "; else if (deviceType == 2) device = "SE8 "; else if (deviceType == 3) device = "DS64 "; else device = "(unknown type) ";
                                int bit = (l.getElement(4) & 0x0E) / 2;
                                int val = (l.getElement(4) & 0x01);
                                int wrd = (l.getElement(4) & 0x70) / 16;
                                int opsw = (l.getElement(4) & 0x7E) / 2 + 1;
                                int bdaddr = l.getElement(2) + 1;
                                if ((l.getElement(1) & 0x1) != 0) bdaddr += 128;
                                String returnVal = device + bdaddr + (((l.getElement(1) & 0x10) != 0) ? " write config bit " : " read config bit ") + wrd + "," + bit + " (opsw " + opsw + ") val=" + val + (val == 1 ? " (closed) " : " (thrown) ");
                                if ((deviceType == 0) && (bdaddr == 0) && (bit == 0) && (val == 0) && (wrd == 0) && (opsw == 1)) {
                                    returnVal += " - Also acts as device query for some device types";
                                }
                                return returnVal + "\n";
                            } else if (pCMD == 0x00) {
                                String device;
                                if ((l.getElement(3) & 0x7) == 0) device = "PM4x "; else if ((l.getElement(3) & 0x7) == 1) device = "BDl16x "; else if ((l.getElement(3) & 0x7) == 2) device = "SE8c "; else if ((l.getElement(3) & 0x7) == 3) device = "DS64 "; else device = "(unknown type) ";
                                int bdaddr = l.getElement(2) + 1;
                                if ((l.getElement(1) & 0x1) != 0) bdaddr += 128;
                                int verNum = l.getElement(4);
                                String versionNumber;
                                if (verNum > 0) versionNumber = Integer.toBinaryString(l.getElement(4)); else versionNumber = "(unknown)";
                                return "Device type report - " + device + bdaddr + "Version " + versionNumber + " is present.\n";
                            } else {
                                forceHex = true;
                                return "OPC_MULTI_SENSE power message PM4 " + (l.getElement(2) + 1) + " unknown CMD=0x" + Integer.toHexString(pCMD) + " ";
                            }
                        case LnConstants.OPC_MULTI_SENSE_PRESENT:
                        case LnConstants.OPC_MULTI_SENSE_ABSENT:
                            String reporterSystemName = "";
                            String reporterUserName = "";
                            String zone;
                            switch(l.getElement(2) & 0x0F) {
                                case 0x00:
                                    zone = "A";
                                    break;
                                case 0x02:
                                    zone = "B";
                                    break;
                                case 0x04:
                                    zone = "C";
                                    break;
                                case 0x06:
                                    zone = "D";
                                    break;
                                case 0x08:
                                    zone = "E";
                                    break;
                                case 0x0A:
                                    zone = "F";
                                    break;
                                case 0x0C:
                                    zone = "G";
                                    break;
                                case 0x0E:
                                    zone = "H";
                                    break;
                                default:
                                    zone = "<unknown " + (l.getElement(2) & 0x0F) + ">";
                                    break;
                            }
                            reporterSystemName = locoNetReporterPrefix + ((l.getElement(1) & 0x1F) * 128 + l.getElement(2) + 1);
                            try {
                                jmri.Reporter reporter = reporterManager.getBySystemName(reporterSystemName);
                                if ((reporter != null) && (reporter.getUserName().length() > 0)) reporterUserName = "(" + reporter.getUserName() + ")"; else reporterUserName = "()";
                            } catch (Exception e) {
                                reporterUserName = "()";
                            }
                            return "Transponder address " + ((l.getElement(3) == 0x7d) ? (l.getElement(4) + " (short)") : (l.getElement(3) * 128 + l.getElement(4) + " (long)")) + ((type == LnConstants.OPC_MULTI_SENSE_PRESENT) ? " present at " : " absent at ") + reporterSystemName + " " + reporterUserName + " (BDL16x Board " + (section + 1) + " RX4 zone " + zone + ").\n";
                        default:
                            forceHex = true;
                            return "OPC_MULTI_SENSE unknown format.\n";
                    }
                }
            case LnConstants.OPC_WR_SL_DATA:
            case LnConstants.OPC_SL_RD_DATA:
                {
                    String mode;
                    String locoAdrStr;
                    String mixedAdrStr;
                    String logString;
                    int command = l.getElement(0);
                    int slot = l.getElement(2);
                    int stat = l.getElement(3);
                    int adr = l.getElement(4);
                    int spd = l.getElement(5);
                    int dirf = l.getElement(6);
                    int trk = l.getElement(7);
                    int ss2 = l.getElement(8);
                    int adr2 = l.getElement(9);
                    int snd = l.getElement(10);
                    int id1 = l.getElement(11);
                    int id2 = l.getElement(12);
                    mixedAdrStr = convertToMixed(adr, adr2);
                    if (adr2 == 0x7f) {
                        if ((ss2 & LnConstants.STAT2_ALIAS_MASK) == LnConstants.STAT2_ID_IS_ALIAS) {
                            locoAdrStr = "" + LOCO_ADR(id2, id1) + " (Alias for loco " + mixedAdrStr + ")";
                        } else {
                            locoAdrStr = mixedAdrStr + " (via Alias)";
                        }
                    } else {
                        locoAdrStr = mixedAdrStr;
                    }
                    if (command == LnConstants.OPC_WR_SL_DATA) {
                        mode = "Request";
                    } else {
                        mode = "Response";
                    }
                    if (slot == LnConstants.FC_SLOT) {
                        int clk_rate = l.getElement(3);
                        int frac_minsl = l.getElement(4);
                        int frac_minsh = l.getElement(5);
                        int mins_60 = l.getElement(6);
                        int track_stat = l.getElement(7);
                        int hours_24 = l.getElement(8);
                        int days = l.getElement(9);
                        int clk_cntrl = l.getElement(10);
                        minutes = ((255 - mins_60) & 0x7f) % 60;
                        hours = ((256 - hours_24) & 0x7f) % 24;
                        hours = (24 - hours) % 24;
                        minutes = (60 - minutes) % 60;
                        frac_mins = 0x3FFF - (frac_minsl + (frac_minsh << 7));
                        if ((trackStatus != track_stat) || showTrackStatus) {
                            trackStatus = track_stat;
                            showStatus = true;
                        }
                        if (showStatus) {
                            logString = mode + " Fast Clock is " + ((clk_cntrl & 0x20) != 0 ? "" : "Synchronized, ") + (clk_rate != 0 ? "Running, " : "Frozen, ") + "rate is " + clk_rate + ":1. Day " + days + ", " + hours + ":" + minutes + "." + frac_mins + ". Last set by ID " + idString(id1, id2) + ".\n\tMaster: " + ((track_stat & LnConstants.GTRK_MLOK1) != 0 ? "LocoNet 1.1" : "DT-200") + "; Track Status: " + ((track_stat & LnConstants.GTRK_POWER) != 0 ? "On" : "Off") + "/" + ((track_stat & LnConstants.GTRK_IDLE) == 0 ? "Paused" : "Running") + "; Programming Track: " + ((track_stat & LnConstants.GTRK_PROG_BUSY) != 0 ? "Busy" : "Available") + "\n";
                        } else {
                            logString = mode + " Fast Clock is " + ((clk_cntrl & 0x20) != 0 ? "" : "Synchronized, ") + (clk_rate != 0 ? "Running, " : "Frozen, ") + "rate is " + clk_rate + ":1. Day " + days + ", " + hours + ":" + minutes + "." + frac_mins + ". Last set by ID " + idString(id1, id2) + ".\n";
                        }
                    } else if (slot == LnConstants.PRG_SLOT) {
                        String operation;
                        String progMode;
                        int cvData;
                        boolean opsMode = false;
                        int cvNumber;
                        int pcmd = l.getElement(3);
                        int pstat = l.getElement(4);
                        int hopsa = l.getElement(5);
                        int lopsa = l.getElement(6);
                        int cvh = l.getElement(8);
                        int cvl = l.getElement(9);
                        int data7 = l.getElement(10);
                        cvData = (((cvh & LnConstants.CVH_D7) << 6) | (data7 & 0x7f));
                        cvNumber = (((((cvh & LnConstants.CVH_CV8_CV9) >> 3) | (cvh & LnConstants.CVH_CV7)) * 128) + (cvl & 0x7f)) + 1;
                        mixedAdrStr = convertToMixed(lopsa, hopsa);
                        if ((pcmd & LnConstants.PCMD_MODE_MASK) == LnConstants.PAGED_ON_SRVC_TRK) {
                            progMode = "Byte in Paged Mode on Service Track";
                        } else if ((pcmd & LnConstants.PCMD_MODE_MASK) == LnConstants.DIR_BYTE_ON_SRVC_TRK) {
                            progMode = "Byte in Direct Mode on Service Track";
                        } else if ((pcmd & LnConstants.PCMD_MODE_MASK) == LnConstants.DIR_BIT_ON_SRVC_TRK) {
                            progMode = "Bits in Direct Mode on Service Track";
                        } else if (((pcmd & ~LnConstants.PCMD_BYTE_MODE) & LnConstants.PCMD_MODE_MASK) == LnConstants.REG_BYTE_RW_ON_SRVC_TRK) {
                            progMode = "Byte in Physical Register R/W Mode on Service Track";
                        } else if ((pcmd & LnConstants.PCMD_MODE_MASK) == LnConstants.OPS_BYTE_NO_FEEDBACK) {
                            progMode = "Byte in OP's Mode (NO feedback)";
                            opsMode = true;
                        } else if ((pcmd & LnConstants.PCMD_MODE_MASK) == LnConstants.OPS_BYTE_FEEDBACK) {
                            progMode = "Byte in OP's Mode";
                            opsMode = true;
                        } else if ((pcmd & LnConstants.PCMD_MODE_MASK) == LnConstants.OPS_BIT_NO_FEEDBACK) {
                            progMode = "Bits in OP's Mode (NO feedback)";
                            opsMode = true;
                        } else if ((pcmd & LnConstants.PCMD_MODE_MASK) == LnConstants.OPS_BIT_FEEDBACK) {
                            progMode = "Bits in OP's Mode";
                            opsMode = true;
                        } else if (((pcmd & ~LnConstants.PCMD_BYTE_MODE) & LnConstants.PCMD_MODE_MASK) == LnConstants.SRVC_TRK_RESERVED) {
                            progMode = "SERVICE TRACK RESERVED MODE DETECTED!";
                        } else {
                            progMode = "Unknown mode " + pcmd + " (0x" + Integer.toHexString(pcmd) + ")";
                            forceHex = true;
                        }
                        if ((pcmd & LnConstants.PCMD_RW) != 0) {
                            operation = "Programming " + mode + ": Write " + progMode;
                            if (opsMode) {
                                logString = operation + " to CV" + cvNumber + " of Loco " + mixedAdrStr + " value " + cvData + " (0x" + Integer.toHexString(cvData) + ", " + Integer.toBinaryString(cvData) + ").\n";
                            } else {
                                logString = operation + " to CV" + cvNumber + " value " + cvData + " (0x" + Integer.toHexString(cvData) + ", " + Integer.toBinaryString(cvData) + ").\n";
                            }
                        } else {
                            operation = "Programming Track " + mode + ": Read " + progMode + " ";
                            if (command == LnConstants.OPC_SL_RD_DATA) {
                                if (pstat != 0) {
                                    if ((pstat & LnConstants.PSTAT_USER_ABORTED) != 0) {
                                        operation += "Failed, User Aborted: ";
                                    }
                                    if ((pstat & LnConstants.PSTAT_READ_FAIL) != 0) {
                                        operation += "Failed, Read Compare Acknowledge not detected: ";
                                    }
                                    if ((pstat & LnConstants.PSTAT_WRITE_FAIL) != 0) {
                                        operation += "Failed, No Write Acknowledge from decoder: ";
                                    }
                                    if ((pstat & LnConstants.PSTAT_NO_DECODER) != 0) {
                                        operation += "Failed, Service Mode programming track empty: ";
                                    }
                                    if ((pstat & 0xF0) != 0) {
                                        operation += "Unable to decode response = 0x" + Integer.toHexString(pstat) + ": ";
                                    }
                                } else {
                                    operation += "Was Successful, set ";
                                }
                            } else {
                                operation += "variable ";
                            }
                            if (opsMode) {
                                logString = operation + " CV" + cvNumber + " of Loco " + mixedAdrStr + " value " + cvData + " (0x" + Integer.toHexString(cvData) + ", " + Integer.toBinaryString(cvData) + ").\n";
                            } else {
                                logString = operation + " CV" + cvNumber + " value " + cvData + " (0x" + Integer.toHexString(cvData) + ", " + Integer.toBinaryString(cvData) + ").\n";
                            }
                        }
                    } else if (slot == LnConstants.CFG_SLOT) {
                        logString = mode + " Comand Station OpSw that are Closed (non-default):\n" + ((l.getElement(3) & 0x80) > 0 ? "\tOpSw1=c, reserved.\n" : "") + ((l.getElement(3) & 0x40) > 0 ? "\tOpSw2=c, DCS100 booster only.\n" : "") + ((l.getElement(3) & 0x20) > 0 ? "\tOpSw3=c, Booster Autoreversing.\n" : "") + ((l.getElement(3) & 0x10) > 0 ? "\tOpSw4=c, reserved.\n" : "") + ((l.getElement(3) & 0x08) > 0 ? "\tOpSw5=c, Master Mode.\n" : "") + ((l.getElement(3) & 0x04) > 0 ? "\tOpSw6=c, reserved.\n" : "") + ((l.getElement(3) & 0x02) > 0 ? "\tOpSw7=c, reserved.\n" : "") + ((l.getElement(3) & 0x01) > 0 ? "\tOpSw8=c, reserved.\n" : "") + ((l.getElement(4) & 0x80) > 0 ? "\tOpSw9=c, Allow Motorola trinary echo 1-256.\n" : "") + ((l.getElement(4) & 0x40) > 0 ? "\tOpSw10=c, Expand trinary switch echo.\n" : "") + ((l.getElement(4) & 0x20) > 0 ? "\tOpSw11=c, Make certian trinary switches long duration.\n" : "") + ((l.getElement(4) & 0x10) > 0 ? "\tOpSw12=c, Trinary addresses 1-80 allowed.\n" : "") + ((l.getElement(4) & 0x08) > 0 ? "\tOpSw13=c, Raise loco address purge time to 600 seconds.\n" : "") + ((l.getElement(4) & 0x04) > 0 ? "\tOpSw14=c, Disable loco address purging.\n" : "") + ((l.getElement(4) & 0x02) > 0 ? "\tOpSw15=c, Purge will force loco to zero speed.\n" : "") + ((l.getElement(4) & 0x01) > 0 ? "\tOpSw16=c, reserved.\n" : "") + ((l.getElement(5) & 0x80) > 0 ? "\tOpSw17=c, Automatic advanced consists are disabled.\n" : "") + ((l.getElement(5) & 0x40) > 0 ? "\tOpSw18=c, Extend booster short shutdown to 1/2 second.\n" : "") + ((l.getElement(5) & 0x20) > 0 ? "\tOpSw19=c, reserved.\n" : "") + ((l.getElement(5) & 0x10) > 0 ? "\tOpSw20=c, Disable address 00 analog operation.\n" : "") + ((l.getElement(5) & 0x08) > 0 ? "\tOpSw21=c, Global default for new loco is FX.\n" : "") + ((l.getElement(5) & 0x04) > 0 ? "\tOpSw22=c, Global default for new loco is 28 step.\n" : "") + ((l.getElement(5) & 0x02) > 0 ? "\tOpSw23=c, Global default for new loco is 14 step.\n" : "") + ((l.getElement(5) & 0x01) > 0 ? "\tOpSw24=c, reserved.\n" : "") + ((l.getElement(6) & 0x80) > 0 ? "\tOpSw25=c, Disable aliasing.\n" : "") + ((l.getElement(6) & 0x40) > 0 ? "\tOpSw26=c, Enable routes.\n" : "") + ((l.getElement(6) & 0x20) > 0 ? "\tOpSw27=c, Disable normal switch commands (Bushby bit).\n" : "") + ((l.getElement(6) & 0x10) > 0 ? "\tOpSw28=c, Disable DS54/64/SE8C interrogate at power on.\n" : "") + ((l.getElement(6) & 0x08) > 0 ? "\tOpSw29=c, reserved.\n" : "") + ((l.getElement(6) & 0x04) > 0 ? "\tOpSw30=c, reserved.\n" : "") + ((l.getElement(6) & 0x02) > 0 ? "\tOpSw31=c, Meter route/switch output when not in trinary.\n" : "") + ((l.getElement(6) & 0x01) > 0 ? "\tOpSw32=c, reserved.\n" : "") + ((l.getElement(8) & 0x80) > 0 ? "\tOpSw33=c, Restore track power to previous state at power on.\n" : "") + ((l.getElement(8) & 0x40) > 0 ? "\tOpSw34=c, Allow track to power up to run state.\n" : "") + ((l.getElement(8) & 0x20) > 0 ? "\tOpSw35=c, reserved.\n" : "") + ((l.getElement(8) & 0x10) > 0 ? "\tOpSw36=c, Clear all moble decoder information and consists.\n" : "") + ((l.getElement(8) & 0x08) > 0 ? "\tOpSw37=c, Clear all routes.\n" : "") + ((l.getElement(8) & 0x04) > 0 ? "\tOpSw38=c, Clear loco roster.\n" : "") + ((l.getElement(8) & 0x02) > 0 ? "\tOpSw39=c, Clear internal memory.\n" : "") + ((l.getElement(8) & 0x01) > 0 ? "\tOpSw40=c, reserved.\n" : "") + ((l.getElement(9) & 0x80) > 0 ? "\tOpSw41=c, Diagnostic click when LocoNet command is received.\n" : "") + ((l.getElement(9) & 0x40) > 0 ? "\tOpSw42=c, Disable 3 beeps when loco address is purged.\n" : "") + ((l.getElement(9) & 0x20) > 0 ? "\tOpSw43=c, Disable LocoNet update of track status.\n" : "") + ((l.getElement(9) & 0x10) > 0 ? "\tOpSw44=c, Expand slots to 120.\n" : "") + ((l.getElement(9) & 0x08) > 0 ? "\tOpSw45=c, Disable replay for switch state request.\n" : "") + ((l.getElement(9) & 0x04) > 0 ? "\tOpSw46=c, reserved.\n" : "") + ((l.getElement(9) & 0x02) > 0 ? "\tOpSw47=c, Programming track is break generator.\n" : "") + ((l.getElement(9) & 0x01) > 0 ? "\tOpSw48=c, reserved.\n" : "") + ((l.getElement(10) & 0x80) > 0 ? "\tOpSw49=c, reserved.\n" : "") + ((l.getElement(10) & 0x40) > 0 ? "\tOpSw50=c, reserved.\n" : "") + ((l.getElement(10) & 0x20) > 0 ? "\tOpSw51=c, reserved.\n" : "") + ((l.getElement(10) & 0x10) > 0 ? "\tOpSw52=c, reserved.\n" : "") + ((l.getElement(10) & 0x08) > 0 ? "\tOpSw53=c, reserved.\n" : "") + ((l.getElement(10) & 0x04) > 0 ? "\tOpSw54=c, reserved.\n" : "") + ((l.getElement(10) & 0x02) > 0 ? "\tOpSw55=c, reserved.\n" : "") + ((l.getElement(10) & 0x01) > 0 ? "\tOpSw56=c, reserved.\n" : "") + ((l.getElement(11) & 0x80) > 0 ? "\tOpSw57=c, reserved.\n" : "") + ((l.getElement(11) & 0x40) > 0 ? "\tOpSw58=c, reserved.\n" : "") + ((l.getElement(11) & 0x20) > 0 ? "\tOpSw59=c, reserved.\n" : "") + ((l.getElement(11) & 0x10) > 0 ? "\tOpSw60=c, reserved.\n" : "") + ((l.getElement(11) & 0x08) > 0 ? "\tOpSw61=c, reserved.\n" : "") + ((l.getElement(11) & 0x04) > 0 ? "\tOpSw62=c, reserved.\n" : "") + ((l.getElement(11) & 0x02) > 0 ? "\tOpSw63=c, reserved.\n" : "") + ((l.getElement(11) & 0x01) > 0 ? "\tOpSw64=c, reserved.\n" : "");
                    } else {
                        if ((trackStatus != trk) || showTrackStatus) {
                            trackStatus = trk;
                            showStatus = true;
                        }
                        if (showStatus) {
                            logString = mode + " slot " + slot + " information:\n\tLoco " + locoAdrStr + " is " + LnConstants.CONSIST_STAT(stat) + ", " + LnConstants.LOCO_STAT(stat) + ", operating in " + LnConstants.DEC_MODE(stat) + " SS mode, and is going " + ((dirf & LnConstants.DIRF_DIR) != 0 ? "in Reverse" : "Foward") + " at speed " + spd + ",\n" + "\tF0=" + ((dirf & LnConstants.DIRF_F0) != 0 ? "On, " : "Off,") + " F1=" + ((dirf & LnConstants.DIRF_F1) != 0 ? "On, " : "Off,") + " F2=" + ((dirf & LnConstants.DIRF_F2) != 0 ? "On, " : "Off,") + " F3=" + ((dirf & LnConstants.DIRF_F3) != 0 ? "On, " : "Off,") + " F4=" + ((dirf & LnConstants.DIRF_F4) != 0 ? "On, " : "Off,") + " Sound1/F5=" + ((snd & LnConstants.SND_F5) != 0 ? "On, " : "Off,") + " Sound2/F6=" + ((snd & LnConstants.SND_F6) != 0 ? "On, " : "Off,") + " Sound3/F7=" + ((snd & LnConstants.SND_F7) != 0 ? "On, " : "Off,") + " Sound4/F8=" + ((snd & LnConstants.SND_F8) != 0 ? "On" : "Off") + "\n\tMaster: " + ((trk & LnConstants.GTRK_MLOK1) != 0 ? "LocoNet 1.1" : "DT-200") + "; Track: " + ((trk & LnConstants.GTRK_IDLE) != 0 ? "On" : "Off") + "; Programming Track: " + ((trk & LnConstants.GTRK_PROG_BUSY) != 0 ? "Busy" : "Available") + "; SS2=0x" + Integer.toHexString(ss2) + ", ThrottleID=" + idString(id1, id2) + "\n";
                        } else {
                            logString = mode + " slot " + slot + " information:\n\tLoco " + locoAdrStr + " is " + LnConstants.CONSIST_STAT(stat) + ", " + LnConstants.LOCO_STAT(stat) + ", operating in " + LnConstants.DEC_MODE(stat) + " SS mode, and is going " + ((dirf & LnConstants.DIRF_DIR) != 0 ? "in Reverse" : "Foward") + " at speed " + spd + ",\n" + "\tF0=" + ((dirf & LnConstants.DIRF_F0) != 0 ? "On, " : "Off,") + " F1=" + ((dirf & LnConstants.DIRF_F1) != 0 ? "On, " : "Off,") + " F2=" + ((dirf & LnConstants.DIRF_F2) != 0 ? "On, " : "Off,") + " F3=" + ((dirf & LnConstants.DIRF_F3) != 0 ? "On, " : "Off,") + " F4=" + ((dirf & LnConstants.DIRF_F4) != 0 ? "On, " : "Off,") + " Sound1/F5=" + ((snd & LnConstants.SND_F5) != 0 ? "On, " : "Off,") + " Sound2/F6=" + ((snd & LnConstants.SND_F6) != 0 ? "On, " : "Off,") + " Sound3/F7=" + ((snd & LnConstants.SND_F7) != 0 ? "On, " : "Off,") + " Sound4/F8=" + ((snd & LnConstants.SND_F8) != 0 ? "On" : "Off") + "\n\tSS2=0x" + Integer.toHexString(ss2) + ", ThrottleID =" + idString(id1, id2) + "\n";
                        }
                    }
                    return logString;
                }
            case LnConstants.OPC_ALM_WRITE:
            case LnConstants.OPC_ALM_READ:
                {
                    String message;
                    if (l.getElement(0) == LnConstants.OPC_ALM_WRITE) {
                        message = "Write ALM msg ";
                    } else {
                        message = "Read ALM msg (Write reply) ";
                    }
                    if (l.getElement(1) == 0x10) {
                        message = message + l.getElement(2) + " ATASK=" + l.getElement(3);
                        if (l.getElement(3) == 2) {
                            message = message + " (RD)";
                        } else if (l.getElement(3) == 3) {
                            message = message + " (WR)";
                        } else if (l.getElement(3) == 0) {
                            message = message + " (ID)";
                        }
                        return message + " BLKL=" + l.getElement(4) + " BLKH=" + l.getElement(5) + " LOGIC=" + l.getElement(6) + "\n      " + " ARG1L=0x" + Integer.toHexString(l.getElement(7)) + " ARG1H=0x" + Integer.toHexString(l.getElement(8)) + " ARG2L=0x" + Integer.toHexString(l.getElement(9)) + " ARG2H=0x" + Integer.toHexString(l.getElement(10)) + "\n      " + " ARG3L=0x" + Integer.toHexString(l.getElement(11)) + " ARG3H=0x" + Integer.toHexString(l.getElement(12)) + " ARG4L=0x" + Integer.toHexString(l.getElement(13)) + " ARG4H=0x" + Integer.toHexString(l.getElement(14)) + "\n";
                    } else if (l.getElement(1) == 0x15) {
                        if (l.getElement(0) == 0xEE) {
                            message = "Write extended slot: ";
                        } else {
                            message = "Read extended slot (Write reply): ";
                        }
                        return message + "slot " + l.getElement(3) + " stat " + l.getElement(4) + " addr " + (l.getElement(6) * 128 + l.getElement(5)) + " speed " + l.getElement(8) + ".\n";
                    } else {
                        return message + " with unexpected length " + l.getElement(1) + ".\n";
                    }
                }
            case LnConstants.OPC_PEER_XFER:
                {
                    switch(l.getElement(1)) {
                        case 0x10:
                            {
                                int src = l.getElement(2);
                                int dst_l = l.getElement(3);
                                int dst_h = l.getElement(4);
                                int pxct1 = l.getElement(5);
                                int pxct2 = l.getElement(10);
                                int d[] = l.getPeerXfrData();
                                String generic = "Peer to Peer transfer: SRC=0x" + Integer.toHexString(src) + ", DSTL=0x" + Integer.toHexString(dst_l) + ", DSTH=0x" + Integer.toHexString(dst_h) + ", PXCT1=0x" + Integer.toHexString(pxct1) + ", PXCT2=0x" + Integer.toHexString(pxct2);
                                String data = "Data [0x" + Integer.toHexString(d[0]) + " 0x" + Integer.toHexString(d[1]) + " 0x" + Integer.toHexString(d[2]) + " 0x" + Integer.toHexString(d[3]) + ",0x" + Integer.toHexString(d[4]) + " 0x" + Integer.toHexString(d[5]) + " 0x" + Integer.toHexString(d[6]) + " 0x" + Integer.toHexString(d[7]) + "]\n";
                                if ((src == 0x7F) && (dst_l == 0x7F) && (dst_h == 0x7F) && ((pxct1 & 0x70) == 0x40)) {
                                    int sub = pxct2 & 0x70;
                                    switch(sub) {
                                        case 0x00:
                                            return "Download message, setup.\n";
                                        case 0x10:
                                            return "Download message, set address " + StringUtil.twoHexFromInt(d[0]) + StringUtil.twoHexFromInt(d[1]) + StringUtil.twoHexFromInt(d[2]) + ".\n";
                                        case 0x20:
                                            return "Download message, send data " + StringUtil.twoHexFromInt(d[0]) + " " + StringUtil.twoHexFromInt(d[1]) + " " + StringUtil.twoHexFromInt(d[2]) + " " + StringUtil.twoHexFromInt(d[3]) + " " + StringUtil.twoHexFromInt(d[4]) + " " + StringUtil.twoHexFromInt(d[5]) + " " + StringUtil.twoHexFromInt(d[6]) + " " + StringUtil.twoHexFromInt(d[7]) + ".\n";
                                        case 0x30:
                                            return "Download message, verify.\n";
                                        case 0x40:
                                            return "Download message, end operation.\n";
                                        default:
                                    }
                                }
                                if (src == 0x50) {
                                    String dst_subaddrx = (dst_h != 0x01 ? "" : ((d[4] != 0) ? "/" + Integer.toHexString(d[4]) : ""));
                                    if (dst_h == 0x01 && ((pxct1 & 0xF0) == 0x00) && ((pxct2 & 0xF0) == 0x10)) {
                                        return "LocoBuffer => LocoIO@" + ((dst_l == 0) ? "broadcast" : Integer.toHexString(dst_l) + dst_subaddrx) + " " + (d[0] == 2 ? "Read SV" + d[1] : "Write SV" + d[1] + "=0x" + Integer.toHexString(d[3])) + ((d[2] != 0) ? " Firmware rev " + dotme(d[2]) : "") + ".\n";
                                    }
                                }
                                if (dst_h == 0x01 && ((pxct1 & 0xF0) == 0x00) && ((pxct2 & 0xF0) == 0x00)) {
                                    String src_subaddrx = ((d[4] != 0) ? "/" + Integer.toHexString(d[4]) : "");
                                    String dst_subaddrx = (dst_h != 0x01 ? "" : ((d[4] != 0) ? "/" + Integer.toHexString(d[4]) : ""));
                                    String src_dev = ((src == 0x50) ? "Locobuffer" : "LocoIO@" + "0x" + Integer.toHexString(src) + src_subaddrx);
                                    String dst_dev = (((dst_h == 0x01) && (dst_l == 0x50)) ? "LocoBuffer " : (((dst_h == 0x01) && (dst_l == 0x0)) ? "broadcast" : "LocoIO@0x" + Integer.toHexString(dst_l) + dst_subaddrx));
                                    return src_dev + "=> " + dst_dev + " " + ((dst_h == 0x01) ? ((d[0] == 2 ? "Read" : "Write") + " SV" + d[1]) : "") + ((src == 0x50) ? (d[0] != 2 ? ("=0x" + Integer.toHexString(d[3])) : "") : " = " + ((d[0] == 2) ? ((d[2] != 0) ? (d[5] < 10) ? "" + d[5] : d[5] + " (0x" + Integer.toHexString(d[5]) + ")" : (d[7] < 10) ? "" + d[7] : d[7] + " (0x" + Integer.toHexString(d[7]) + ")") : (d[7] < 10) ? "" + d[7] : d[7] + " (0x" + Integer.toHexString(d[7]) + ")")) + ((d[2] != 0) ? " Firmware rev " + dotme(d[2]) : "") + ".\n";
                                }
                                if (((pxct1 & 0xF0) == 0x10) && ((pxct2 & 0xF0) == 0x10)) {
                                    return "SV Programming Protocol v2: " + generic + "\n\t" + data;
                                }
                                return generic + "\n\t" + data;
                            }
                        case 0x0A:
                            {
                                int tcntrl = l.getElement(2);
                                String stat;
                                if (tcntrl == 0x40) stat = " (OK) "; else if (tcntrl == 0x7F) stat = " (no key, immed, ignored) "; else if (tcntrl == 0x43) stat = " (+ key during msg) "; else if (tcntrl == 0x42) stat = " (- key during msg) "; else if (tcntrl == 0x41) stat = " (R/S key during msg, aborts) "; else stat = " (unknown) ";
                                return "Throttle status TCNTRL=" + Integer.toHexString(tcntrl) + stat + " ID1,ID2=" + Integer.toHexString(l.getElement(3)) + Integer.toHexString(l.getElement(4)) + " SLA=" + Integer.toHexString(l.getElement(7)) + " SLB=" + Integer.toHexString(l.getElement(8)) + ".\n";
                            }
                        case 0x14:
                            {
                                switch(l.getElement(2)) {
                                    case 0x01:
                                        {
                                            switch(l.getElement(3)) {
                                                case 0x08:
                                                    {
                                                        return "Query Duplex Receivers.\n";
                                                    }
                                                case 0x10:
                                                    {
                                                        return "Duplex Receiver Response.\n";
                                                    }
                                                default:
                                                    {
                                                        forceHex = true;
                                                        return "Unknown Duplex Channel message.\n";
                                                    }
                                            }
                                        }
                                    case 0x02:
                                        {
                                            switch(l.getElement(3)) {
                                                case 0x00:
                                                    {
                                                        int channel = l.getElement(5) | ((l.getElement(4) & 0x01) << 7);
                                                        return "Set Duplex Channel to " + Integer.toString(channel) + ".\n";
                                                    }
                                                case 0x08:
                                                    {
                                                        return "Query Duplex Channel.\n";
                                                    }
                                                case 0x10:
                                                    {
                                                        int channel = l.getElement(5) | ((l.getElement(4) & 0x01) << 7);
                                                        return "Reported Duplex Channel is " + Integer.toString(channel) + ".\n";
                                                    }
                                                default:
                                                    {
                                                        forceHex = true;
                                                        return "Unknown Duplex Channel message.\n";
                                                    }
                                            }
                                        }
                                    case 0x03:
                                        {
                                            char[] groupNameArray = { (char) (l.getElement(5) | ((l.getElement(4) & 0x01) << 7)), (char) (l.getElement(6) | ((l.getElement(4) & 0x02) << 6)), (char) (l.getElement(7) | ((l.getElement(4) & 0x04) << 5)), (char) (l.getElement(8) | ((l.getElement(4) & 0x08) << 4)), (char) (l.getElement(10) | ((l.getElement(9) & 0x01) << 7)), (char) (l.getElement(11) | ((l.getElement(9) & 0x02) << 6)), (char) (l.getElement(12) | ((l.getElement(9) & 0x04) << 5)), (char) (l.getElement(13) | ((l.getElement(9) & 0x08) << 4)) };
                                            String groupName = new String(groupNameArray);
                                            int p1 = ((l.getElement(14) & 0x01) << 3) | ((l.getElement(15) & 0x70) >> 4);
                                            int p2 = l.getElement(15) & 0x0F;
                                            int p3 = ((l.getElement(14) & 0x02) << 2) | ((l.getElement(16) & 0x70) >> 4);
                                            int p4 = l.getElement(16) & 0x0F;
                                            String passcode = Integer.toHexString(p1) + Integer.toHexString(p2) + Integer.toHexString(p3) + Integer.toHexString(p4);
                                            int channel = l.getElement(17) | ((l.getElement(14) & 0x04) << 5);
                                            int id = l.getElement(18) | ((l.getElement(14) & 0x08) << 4);
                                            switch(l.getElement(3)) {
                                                case 0x00:
                                                    {
                                                        return "Set Duplex Group Name to '" + groupName + ".\n";
                                                    }
                                                case 0x08:
                                                    {
                                                        return "Query Duplex Group Information.\n";
                                                    }
                                                case 0x10:
                                                    {
                                                        return "Reported Duplex Group Name is '" + groupName + "', Password " + passcode + ", Channel " + Integer.toString(channel) + ", ID " + Integer.toString(id) + ".\n";
                                                    }
                                                default:
                                                    {
                                                        forceHex = true;
                                                        return "Unknown Duplex Group Name message.\n";
                                                    }
                                            }
                                        }
                                    case 0x04:
                                        {
                                            int id = l.getElement(5) | ((l.getElement(4) & 0x01) << 7);
                                            switch(l.getElement(3)) {
                                                case 0x00:
                                                    {
                                                        return "Set Duplex Group ID to '" + Integer.toString(id) + ".\n";
                                                    }
                                                case 0x08:
                                                    {
                                                        return "Query Duplex Group ID.\n";
                                                    }
                                                case 0x10:
                                                    {
                                                        return "Reported Duplex Group ID is " + Integer.toString(id) + ".\n";
                                                    }
                                                default:
                                                    {
                                                        forceHex = true;
                                                        return "Unknown Duplex Group ID message.\n";
                                                    }
                                            }
                                        }
                                    case 0x07:
                                        {
                                            char[] groupPasswordArray = { (char) l.getElement(5), (char) l.getElement(6), (char) l.getElement(7), (char) l.getElement(8) };
                                            String groupPassword = new String(groupPasswordArray);
                                            switch(l.getElement(3)) {
                                                case 0x00:
                                                    {
                                                        return "Set Duplex Group Password is '" + groupPassword + "'.\n";
                                                    }
                                                case 0x08:
                                                    {
                                                        return "Query Duplex Group Password.\n";
                                                    }
                                                case 0x10:
                                                    {
                                                        return "Reported Duplex Group Password is '" + groupPassword + "'.\n";
                                                    }
                                                default:
                                                    {
                                                        forceHex = true;
                                                        return "Unknown Duplex Group Password message.\n";
                                                    }
                                            }
                                        }
                                    case 0x10:
                                        {
                                            switch(l.getElement(3)) {
                                                case 0x08:
                                                    {
                                                        return "Query Duplex Channel " + Integer.toString(l.getElement(5)) + " noise/activity report.\n";
                                                    }
                                                case 0x10:
                                                    {
                                                        int level = (l.getElement(6) & 0x7F) | ((l.getElement(4) & 0x02) << 6);
                                                        return "Reported Duplex Channel " + Integer.toString(l.getElement(5)) + " noise/activity level is " + Integer.toString(level) + "/255.\n";
                                                    }
                                                default:
                                                    {
                                                        forceHex = true;
                                                        return "Unknown Duplex Channel Activity message.\n";
                                                    }
                                            }
                                        }
                                    case LnConstants.RE_IPL_PING_OPERATION:
                                        {
                                            String interpretedMessage;
                                            if (l.getElement(3) == 0x08) {
                                                if ((((l.getElement(4) & 0xF) != 0) || (l.getElement(5) != 0) || (l.getElement(6) != 0) || (l.getElement(7) != 0) || (l.getElement(8) != 0)) && (l.getElement(9) == 0) && (l.getElement(10) == 0) && (l.getElement(11) == 0) && (l.getElement(12) == 0) && (l.getElement(13) == 0) && (l.getElement(14) == 0) && (l.getElement(15) == 0) && (l.getElement(16) == 0) && (l.getElement(17) == 0) && (l.getElement(18) == 0)) {
                                                    interpretedMessage = "Ping request.\n";
                                                    int hostSnInt = 0;
                                                    hostSnInt = (l.getElement(5) + (((l.getElement(4) & 0x1) == 1) ? 128 : 0)) + ((l.getElement(6) + (((l.getElement(4) & 0x2) == 2) ? 128 : 0)) * 256) + ((l.getElement(7) + (((l.getElement(4) & 0x4) == 4) ? 128 : 0)) * 256 * 256) + ((l.getElement(8) + (((l.getElement(4) & 0x8) == 8) ? 128 : 0)) * 256 * 256 * 256);
                                                    interpretedMessage += "\tPinging device with serial number " + Integer.toHexString(hostSnInt).toUpperCase() + "\n";
                                                    return interpretedMessage;
                                                } else {
                                                    forceHex = true;
                                                    return "Message with opcode 0xE5 and unknown format.";
                                                }
                                            } else if (l.getElement(3) == 0x10) {
                                                if (((l.getElement(4) & 0xF) != 0) || (l.getElement(5) != 0) || (l.getElement(6) != 0) || (l.getElement(7) != 0) || (l.getElement(8) != 0)) {
                                                    interpretedMessage = "Ping Report.\n";
                                                    int hostSnInt = 0;
                                                    hostSnInt = (l.getElement(5) + (((l.getElement(4) & 0x1) == 1) ? 128 : 0)) + ((l.getElement(6) + (((l.getElement(4) & 0x2) == 2) ? 128 : 0)) * 256) + ((l.getElement(7) + (((l.getElement(4) & 0x4) == 4) ? 128 : 0)) * 256 * 256) + ((l.getElement(8) + (((l.getElement(4) & 0x8) == 8) ? 128 : 0)) * 256 * 256 * 256);
                                                    interpretedMessage += "\tPing response from device with serial number " + Integer.toHexString(hostSnInt).toUpperCase() + " Local RSSI = 0x" + Integer.toHexString(l.getElement(12) + (((l.getElement(9)) & 0x4) == 0x4 ? 128 : 0)).toUpperCase() + " Remote RSSI = 0x" + Integer.toHexString(l.getElement(13) + (((l.getElement(9)) & 0x8) == 0x8 ? 128 : 0)).toUpperCase() + ".\n";
                                                    return interpretedMessage;
                                                } else {
                                                    forceHex = true;
                                                    return "Message with opcode 0xE5 and unknown format.";
                                                }
                                            } else {
                                                forceHex = true;
                                                return "Message with opcode 0xE5 and unknown format.";
                                            }
                                        }
                                    case LnConstants.RE_IPL_IDENTITY_OPERATION:
                                        {
                                            String interpretedMessage;
                                            String device = "";
                                            switch(l.getElement(3)) {
                                                case 0x08:
                                                    {
                                                        if ((l.getElement(4) == 0) && (l.getElement(5) == 0) && (l.getElement(6) == 0) && (l.getElement(7) == 0) && (l.getElement(8) == 0) && (l.getElement(9) == 0) && (l.getElement(10) == 0) && (l.getElement(11) == 1) && (l.getElement(12) == 0) && (l.getElement(13) == 0) && (l.getElement(14) == 0) && (l.getElement(15) == 0) && (l.getElement(16) == 0) && (l.getElement(17) == 0) && (l.getElement(18) == 0)) {
                                                            return "Discover all IPL-capable devices request.\n";
                                                        } else if (((l.getElement(5) != 0) || (l.getElement(6) != 0))) {
                                                            device = getDeviceNameFromIPLInfo(l.getElement(4), l.getElement(5));
                                                            String slave = getSlaveNameFromIPLInfo(l.getElement(4), l.getElement(6));
                                                            interpretedMessage = "Discover " + device + " devices and/or " + slave + " devices.\n";
                                                            return interpretedMessage;
                                                        } else {
                                                            forceHex = true;
                                                            return "Message with opcode 0xE5 and unknown format.";
                                                        }
                                                    }
                                                case 0x10:
                                                    {
                                                        interpretedMessage = "IPL Identity report.\n";
                                                        String HostType = getDeviceNameFromIPLInfo(l.getElement(4), l.getElement(5));
                                                        String HostVer = ((l.getElement(8) & 0x78) >> 3) + "." + ((l.getElement(8) & 0x7));
                                                        int hostSnInt = ((l.getElement(13) + (((l.getElement(9) & 0x8) == 8) ? 128 : 0)) * 256 * 256) + ((l.getElement(12) + (((l.getElement(9) & 0x4) == 4) ? 128 : 0)) * 256) + (l.getElement(11) + (((l.getElement(9) & 0x2) == 2) ? 128 : 0));
                                                        String HostSN = Integer.toHexString(hostSnInt).toUpperCase();
                                                        String SlaveType = getSlaveNameFromIPLInfo(l.getElement(4), l.getElement(6));
                                                        String SlaveVer = "";
                                                        String SlaveSN = "";
                                                        if (l.getElement(6) > 0) {
                                                            SlaveVer = (((l.getElement(10) & 0x78) >> 3) + ((l.getElement(9) & 1) << 4)) + "." + ((l.getElement(10) & 0x7));
                                                            int slaveSnInt = ((l.getElement(15) + (((l.getElement(14) & 0x1) == 1) ? 128 : 0))) + ((l.getElement(16) + (((l.getElement(14) & 0x2) == 2) ? 128 : 0)) * 256) + ((l.getElement(17) + (((l.getElement(14) & 0x4) == 4) ? 128 : 0)) * 256 * 256) + ((l.getElement(18) + (((l.getElement(14) & 0x8) == 8) ? 128 : 0)) * 256 * 256 * 256);
                                                            SlaveSN = Integer.toHexString(slaveSnInt).toUpperCase();
                                                        } else {
                                                            SlaveVer = "N/A";
                                                            SlaveSN = "N/A";
                                                        }
                                                        interpretedMessage += "\tHost: " + HostType + ", S/N: " + HostSN + ", S/W Version: " + HostVer + "\n\tSlave: " + SlaveType + ", S/N: " + SlaveSN + ", S/W Version: " + SlaveVer + "\n";
                                                        return interpretedMessage;
                                                    }
                                                default:
                                                    {
                                                        forceHex = true;
                                                        return "Message with opcode 0xE5 and unknown format.";
                                                    }
                                            }
                                        }
                                    default:
                                        {
                                            forceHex = true;
                                            return "Message with opcode 0xE5 and unknown format.";
                                        }
                                }
                            }
                        case 0x09:
                            {
                                switch(l.getElement(2)) {
                                    case 0x40:
                                        {
                                            int locoAddr = l.getElement(4);
                                            if (l.getElement(3) != 0x7d) {
                                                locoAddr += l.getElement(3) << 7;
                                            }
                                            return "Transponding Find query for loco address " + locoAddr + ".\n";
                                        }
                                    case 0x00:
                                        {
                                            int section = ((l.getElement(5) & 0x1F) << 3) + ((l.getElement(6) & 0x70) >> 4) + 1;
                                            String zone;
                                            int locoAddr = l.getElement(4);
                                            if (l.getElement(3) != 0x7d) {
                                                locoAddr += l.getElement(3) << 7;
                                            }
                                            switch(l.getElement(6) & 0x0F) {
                                                case 0x00:
                                                    zone = "A";
                                                    break;
                                                case 0x02:
                                                    zone = "B";
                                                    break;
                                                case 0x04:
                                                    zone = "C";
                                                    break;
                                                case 0x06:
                                                    zone = "D";
                                                    break;
                                                case 0x08:
                                                    zone = "E";
                                                    break;
                                                case 0x0A:
                                                    zone = "F";
                                                    break;
                                                case 0x0C:
                                                    zone = "G";
                                                    break;
                                                case 0x0E:
                                                    zone = "H";
                                                    break;
                                                default:
                                                    zone = "<unknown " + (l.getElement(2) & 0x0F) + ">";
                                                    break;
                                            }
                                            String reporterSystemName = "";
                                            String reporterUserName = "";
                                            reporterSystemName = locoNetReporterPrefix + ((l.getElement(5) & 0x1F) * 128 + l.getElement(6) + 1);
                                            try {
                                                jmri.Reporter reporter = reporterManager.getBySystemName(reporterSystemName);
                                                if ((reporter != null) && (reporter.getUserName().length() > 0)) reporterUserName = "(" + reporter.getUserName() + ")"; else reporterUserName = "()";
                                            } catch (Exception e) {
                                                reporterUserName = "()";
                                            }
                                            return "Transponder Find report : address " + locoAddr + ((l.getElement(3) == 0x7d) ? " (short)" : " (long)") + " present at " + reporterSystemName + " " + reporterUserName + " (BDL16x Board " + (section + 1) + " RX4 zone " + zone + ").\n";
                                        }
                                    default:
                                        {
                                            forceHex = true;
                                            return "Message with opcode 0xE5 and unknown format.";
                                        }
                                }
                            }
                        default:
                            {
                                forceHex = true;
                                return "Message with opcode 0xE5 and unknown format.";
                            }
                    }
                }
            case LnConstants.OPC_LISSY_UPDATE:
                {
                    switch(l.getElement(1)) {
                        case 0x08:
                            int unit = (l.getElement(4) & 0x7F);
                            int address = (l.getElement(6) & 0x7F) + 128 * (l.getElement(5) & 0x7F);
                            if (l.getElement(2) == 0x00) {
                                return "Lissy " + unit + " IR Report: Loco " + address + " moving " + ((l.getElement(3) & 0x20) == 0 ? "north\n" : "south\n");
                            } else if (l.getElement(2) == 0x01) {
                                int wheelCount = (l.getElement(6) & 0x7F) + 128 * (l.getElement(5) & 0x7F);
                                return "Lissy " + unit + " Wheel Report: " + wheelCount + " wheels moving " + ((l.getElement(3) & 0x20) == 0 ? "north\n" : "south\n");
                            } else {
                                forceHex = true;
                                return "Unrecognized Lissy message varient.\n";
                            }
                        case 0x0A:
                            int element = l.getElement(2) * 128 + l.getElement(3);
                            int stat1 = l.getElement(5);
                            int stat2 = l.getElement(6);
                            String status;
                            if ((stat1 & 0x10) != 0) if ((stat1 & 0x20) != 0) status = " AX, XA reserved; "; else status = " AX reserved; "; else if ((stat1 & 0x20) != 0) status = " XA reserved; "; else status = " no reservation; ";
                            if ((stat2 & 0x01) != 0) status += "Turnout thrown; "; else status += "Turnout closed; ";
                            if ((stat1 & 0x01) != 0) status += "Occupied"; else status += "Not occupied";
                            return "SE" + (element + 1) + " (" + element + ") reports AX:" + l.getElement(7) + " XA:" + l.getElement(8) + status + "\n";
                        default:
                            forceHex = true;
                            return "Unrecognized OPC_LISSY_UPDATE command varient.\n";
                    }
                }
            case LnConstants.OPC_IMM_PACKET:
                {
                    int val7f = l.getElement(2);
                    int reps = l.getElement(3);
                    int dhi = l.getElement(4);
                    int im1 = l.getElement(5);
                    int im2 = l.getElement(6);
                    int im3 = l.getElement(7);
                    int im4 = l.getElement(8);
                    int im5 = l.getElement(9);
                    int mobileDecoderAddress = -999;
                    int nmraInstructionType = -999;
                    int nmraSubInstructionType = -999;
                    int playableWhistleLevel = -999;
                    if (val7f == 0x7f) {
                        int len = ((reps & 0x70) >> 4);
                        byte[] packet = new byte[len];
                        int[] packetInt = new int[len];
                        packet[0] = (byte) (im1 + ((dhi & 0x01) != 0 ? 0x80 : 0));
                        packetInt[0] = (im1 + ((dhi & 0x01) != 0 ? 0x80 : 0));
                        if (len >= 2) {
                            packet[1] = (byte) (im2 + ((dhi & 0x02) != 0 ? 0x80 : 0));
                            packetInt[1] = (im2 + ((dhi & 0x02) != 0 ? 0x80 : 0));
                        }
                        if (len >= 3) {
                            packet[2] = (byte) (im3 + ((dhi & 0x04) != 0 ? 0x80 : 0));
                            packetInt[2] = (im3 + ((dhi & 0x04) != 0 ? 0x80 : 0));
                        }
                        if (len >= 4) {
                            packet[3] = (byte) (im4 + ((dhi & 0x08) != 0 ? 0x80 : 0));
                            packetInt[3] = (im4 + ((dhi & 0x08) != 0 ? 0x80 : 0));
                        }
                        if (len >= 5) {
                            packet[4] = (byte) (im5 + ((dhi & 0x10) != 0 ? 0x80 : 0));
                            packetInt[4] = (im5 + ((dhi & 0x10) != 0 ? 0x80 : 0));
                        }
                        int address;
                        if ((packetInt[0] & 0x80) == 0x0) {
                            mobileDecoderAddress = packetInt[0];
                            nmraInstructionType = (packetInt[1] & 0xE) >> 5;
                            nmraSubInstructionType = (packetInt[1] & 0x1f);
                            if ((nmraSubInstructionType == 0x1d) && (packetInt[2] == 0x7f)) {
                                playableWhistleLevel = packetInt[3];
                            }
                        } else if ((packetInt[0] & 0xC0) == 0xC0) {
                            mobileDecoderAddress = ((packetInt[0] & 0x3F) << 8) + packetInt[1];
                            nmraInstructionType = (packetInt[2] & 0xE0) >> 5;
                            nmraSubInstructionType = (packetInt[2] & 0x1f);
                            if ((nmraSubInstructionType == 0x1d) && (packetInt[3] == 0x7f)) {
                                playableWhistleLevel = packetInt[4];
                            }
                        } else {
                        }
                        String generic = "";
                        if ((mobileDecoderAddress >= 0) && (nmraInstructionType == 1) && (nmraSubInstructionType == 0x1D)) {
                            generic = "Playable Whistle control - Loco " + mobileDecoderAddress + " whistle to " + playableWhistleLevel + " (repeat " + (reps & 0x7) + " times).\n";
                            return generic;
                        }
                        generic = "Send packet immediate: " + ((reps & 0x70) >> 4) + " bytes, repeat count " + (reps & 0x07) + "(" + reps + ")" + "\n\tDHI=0x" + Integer.toHexString(dhi) + ", IM1=0x" + Integer.toHexString(im1) + ", IM2=0x" + Integer.toHexString(im2) + ", IM3=0x" + Integer.toHexString(im3) + ", IM4=0x" + Integer.toHexString(im4) + ", IM5=0x" + Integer.toHexString(im5) + "\n\tpacket: ";
                        if ((packetInt[0] & 0xC0) == 0xC0) {
                            address = ((packetInt[0] & 0x3F) << 8) + packetInt[1];
                            if ((packetInt[2] & 0xFF) == 0xDF) {
                                return "Send packet immediate: Locomotive " + address + " set" + " F21=" + ((packetInt[3] & 0x01) > 0 ? "On" : "Off") + ", F22=" + ((packetInt[3] & 0x02) > 0 ? "On" : "Off") + ", F23=" + ((packetInt[3] & 0x04) > 0 ? "On" : "Off") + ", F24=" + ((packetInt[3] & 0x08) > 0 ? "On" : "Off") + ", F25=" + ((packetInt[3] & 0x10) > 0 ? "On" : "Off") + ", F26=" + ((packetInt[3] & 0x20) > 0 ? "On" : "Off") + ", F27=" + ((packetInt[3] & 0x40) > 0 ? "On" : "Off") + ", F28=" + ((packetInt[3] & 0x80) > 0 ? "On" : "Off") + "\n";
                            } else if ((packetInt[2] & 0xFF) == 0xDE) {
                                return "Send packet immediate: Locomotive " + address + " set" + " F13=" + ((packetInt[3] & 0x01) > 0 ? "On" : "Off") + ", F14=" + ((packetInt[3] & 0x02) > 0 ? "On" : "Off") + ", F15=" + ((packetInt[3] & 0x04) > 0 ? "On" : "Off") + ", F16=" + ((packetInt[3] & 0x08) > 0 ? "On" : "Off") + ", F17=" + ((packetInt[3] & 0x10) > 0 ? "On" : "Off") + ", F18=" + ((packetInt[3] & 0x20) > 0 ? "On" : "Off") + ", F19=" + ((packetInt[3] & 0x40) > 0 ? "On" : "Off") + ", F20=" + ((packetInt[3] & 0x80) > 0 ? "On" : "Off") + "\n";
                            } else if ((packetInt[2] & 0xF0) == 0xA0) {
                                return "Send packet immediate: Locomotive " + address + " set" + ", F09=" + ((packetInt[2] & 0x01) > 0 ? "On" : "Off") + ", F10=" + ((packetInt[2] & 0x02) > 0 ? "On" : "Off") + ", F11=" + ((packetInt[2] & 0x04) > 0 ? "On" : "Off") + ", F12=" + ((packetInt[2] & 0x08) > 0 ? "On" : "Off") + "\n";
                            } else {
                                return generic + jmri.NmraPacket.format(packet) + "\n";
                            }
                        } else {
                            address = packetInt[0];
                            if ((packetInt[1] & 0xFF) == 0xDF) {
                                return "Send packet immediate: Locomotive " + address + " set" + " F21=" + ((packetInt[2] & 0x01) > 0 ? "On" : "Off") + ", F22=" + ((packetInt[2] & 0x02) > 0 ? "On" : "Off") + ", F23=" + ((packetInt[2] & 0x04) > 0 ? "On" : "Off") + ", F24=" + ((packetInt[2] & 0x08) > 0 ? "On" : "Off") + ", F25=" + ((packetInt[2] & 0x10) > 0 ? "On" : "Off") + ", F26=" + ((packetInt[2] & 0x20) > 0 ? "On" : "Off") + ", F27=" + ((packetInt[2] & 0x40) > 0 ? "On" : "Off") + ", F28=" + ((packetInt[2] & 0x80) > 0 ? "On" : "Off") + "\n";
                            } else if ((packetInt[1] & 0xFF) == 0xDE) {
                                return "Send packet immediate: Locomotive " + address + " set" + " F13=" + ((packetInt[2] & 0x01) > 0 ? "On" : "Off") + ", F14=" + ((packetInt[2] & 0x02) > 0 ? "On" : "Off") + ", F15=" + ((packetInt[2] & 0x04) > 0 ? "On" : "Off") + ", F16=" + ((packetInt[2] & 0x08) > 0 ? "On" : "Off") + ", F17=" + ((packetInt[2] & 0x10) > 0 ? "On" : "Off") + ", F18=" + ((packetInt[2] & 0x20) > 0 ? "On" : "Off") + ", F19=" + ((packetInt[2] & 0x40) > 0 ? "On" : "Off") + ", F20=" + ((packetInt[2] & 0x80) > 0 ? "On" : "Off") + "\n";
                            } else if ((packetInt[1] & 0xF0) == 0xA0) {
                                return "Send packet immediate: Locomotive " + address + " set" + " F09=" + ((packetInt[1] & 0x01) > 0 ? "On" : "Off") + ", F10=" + ((packetInt[1] & 0x02) > 0 ? "On" : "Off") + ", F11=" + ((packetInt[1] & 0x04) > 0 ? "On" : "Off") + ", F12=" + ((packetInt[1] & 0x08) > 0 ? "On" : "Off") + "\n";
                            } else {
                                return generic + jmri.NmraPacket.format(packet) + "\n";
                            }
                        }
                    } else {
                        forceHex = true;
                        return "Undefined Send Packet Immediate, 3rd byte id 0x" + Integer.toHexString(val7f) + " not 0x7f.\n";
                    }
                }
            case LnConstants.RE_OPC_PR3_MODE:
                {
                    if ((l.getElement(1) == 0x10) && ((l.getElement(2) & 0x7c) == 0) && (l.getElement(3) == 0) && (l.getElement(4) == 0)) {
                        switch(l.getElement(2) & 0x3) {
                            case 0x00:
                                {
                                    return "Set PR3 to MS100 mode without PR3 termination of LocoNet (i.e. use PR3 with command station present).\n";
                                }
                            case 0x01:
                                {
                                    return "Set PR3 to decoder programming track mode (i.e. no command station present).\n";
                                }
                            case 0x03:
                                {
                                    return "Set PR3 to MS100 mode with PR3 termination of LocoNet (i.e. use PR3 without command station present).\n";
                                }
                            default:
                                {
                                    return "Set PR3 to (not understood) mode.\n";
                                }
                        }
                    } else {
                        forceHex = true;
                        return "Unable to parse command.\n";
                    }
                }
            default:
                forceHex = true;
                return "Unable to parse command.\n";
        }
    }
