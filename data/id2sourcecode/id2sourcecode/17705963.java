    public final void write(final int reg, final int data) {
        updateto((int) cpu.cycles - 1);
        switch(reg) {
            case 0x0:
                lenctrHalt[0] = utils.getbit(data, 5);
                timers[0].setduty(dutylookup[data >> 6]);
                envConstVolume[0] = utils.getbit(data, 4);
                envelopeValue[0] = data & 15;
                break;
            case 0x1:
                sweepenable[0] = utils.getbit(data, 7);
                sweepperiod[0] = (data >> 4) & 7;
                sweepnegate[0] = utils.getbit(data, 3);
                sweepshift[0] = (data & 7);
                sweepreload[0] = true;
                break;
            case 0x2:
                timers[0].setperiod((timers[0].getperiod() & 0xfe00) + (data << 1));
                break;
            case 0x3:
                if (lenCtrEnable[0]) {
                    lengthctr[0] = lenctrload[data >> 3];
                }
                timers[0].setperiod((timers[0].getperiod() & 0x1ff) + ((data & 7) << 9));
                timers[0].reset();
                envelopeStartFlag[0] = true;
                break;
            case 0x4:
                lenctrHalt[1] = utils.getbit(data, 5);
                timers[1].setduty(dutylookup[data >> 6]);
                envConstVolume[1] = utils.getbit(data, 4);
                envelopeValue[1] = data & 15;
                break;
            case 0x5:
                sweepenable[1] = utils.getbit(data, 7);
                sweepperiod[1] = (data >> 4) & 7;
                sweepnegate[1] = utils.getbit(data, 3);
                sweepshift[1] = (data & 7);
                sweepreload[1] = true;
                break;
            case 0x6:
                timers[1].setperiod((timers[1].getperiod() & 0xfe00) + (data << 1));
                break;
            case 0x7:
                if (lenCtrEnable[1]) {
                    lengthctr[1] = lenctrload[data >> 3];
                }
                timers[1].setperiod((timers[1].getperiod() & 0x1ff) + ((data & 7) << 9));
                timers[1].reset();
                envelopeStartFlag[1] = true;
                break;
            case 0x8:
                linctrreload = data & 0x7f;
                lenctrHalt[2] = utils.getbit(data, 7);
                break;
            case 0x9:
                break;
            case 0xA:
                timers[2].setperiod((((timers[2].getperiod() * 1) & 0xff00) + data) / 1);
                break;
            case 0xB:
                if (lenCtrEnable[2]) {
                    lengthctr[2] = lenctrload[data >> 3];
                }
                timers[2].setperiod((((timers[2].getperiod() * 1) & 0xff) + ((data & 7) << 8)) / 1);
                linctrflag = true;
                break;
            case 0xC:
                lenctrHalt[3] = utils.getbit(data, 5);
                envConstVolume[3] = utils.getbit(data, 4);
                envelopeValue[3] = data & 0xf;
                break;
            case 0xD:
                break;
            case 0xE:
                timers[3].setduty(utils.getbit(data, 7) ? 6 : 1);
                timers[3].setperiod(noiseperiod[data & 15]);
                break;
            case 0xF:
                if (lenCtrEnable[3]) {
                    lengthctr[3] = lenctrload[data >> 3];
                }
                envelopeStartFlag[3] = true;
                break;
            case 0x10:
                dmcirq = utils.getbit(data, 7);
                dmcloop = utils.getbit(data, 6);
                dmcrate = dmcperiods[data & 0xf];
                break;
            case 0x11:
                dmcvalue = data;
                if (dmcvalue > 0x7f) {
                    dmcvalue = 0x7f;
                }
                if (dmcvalue < 0) {
                    dmcvalue = 0;
                }
                break;
            case 0x12:
                dmcstartaddr = (data << 6) + 0xc000;
                break;
            case 0x13:
                dmcsamplelength = (data << 4) + 1;
                break;
            case 0x14:
                cpuram.write(0x2003, 0);
                for (int i = 0; i < 256; ++i) {
                    cpuram.write(0x2004, cpuram.read((data << 8) + i));
                }
                cpu.cycles += 513;
                break;
            case 0x15:
                for (int i = 0; i < 4; ++i) {
                    lenCtrEnable[i] = utils.getbit(data, i);
                    if (!lenCtrEnable[i]) {
                        lengthctr[i] = 0;
                    }
                }
                if (utils.getbit(data, 4)) {
                    if (dmcsamplesleft == 0) {
                        restartdmc();
                    }
                } else {
                    dmcsamplesleft = 0;
                    dmcsilence = true;
                }
                if (statusdmcint) {
                    --cpu.interrupt;
                }
                statusdmcint = false;
                break;
            case 0x16:
                nes.getcontroller1().output(utils.getbit(data, 0));
                nes.getcontroller2().output(utils.getbit(data, 0));
                break;
            case 0x17:
                ctrmode = utils.getbit(data, 7) ? 5 : 4;
                apuintflag = utils.getbit(data, 6);
                framectr = 0;
                if (utils.getbit(data, 7)) {
                    clockframecounter();
                }
                framectroffset = cpu.cycles % 7445;
                if ((ctrmode == 5 || apuintflag) && statusframeint) {
                    statusframeint = false;
                    --cpu.interrupt;
                }
                break;
        }
    }
