    public int write(DataOutputStream dos) throws IOException {
        int bytes_out = jm.midi.MidiUtil.writeVarLength(this.time, dos);
        dos.writeByte(0xFF);
        dos.writeByte(0x58);
        bytes_out += jm.midi.MidiUtil.writeVarLength(4, dos);
        dos.writeByte((byte) this.numerator);
        int num = this.denominator;
        int cnt = 0;
        while (num % 2 == 0) {
            num = num / 2;
            cnt++;
        }
        dos.writeByte((byte) cnt);
        dos.writeByte(0x18);
        dos.writeByte(0x08);
        return bytes_out + 6;
    }
