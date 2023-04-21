    protected void printACMcode() {
        FileUtils f = new FileUtils();
        if (this.a_cpp && this.cppcode != null) {
            f.writeTextFile("Reader" + ACMgenOptions.cpp_ext, this.cppcode.getReader_cpp());
            f.writeTextFile("Reader" + ACMgenOptions.h_ext, this.cppcode.getReader_h());
            f.writeTextFile("Writer" + ACMgenOptions.cpp_ext, this.cppcode.getWriter_cpp());
            f.writeTextFile("Writer" + ACMgenOptions.h_ext, this.cppcode.getWriter_h());
        } else if (this.a_cpp && this.cppcode == null) {
            this.printErrorMsg(ACMgen.msg.getMessage("cppcode_error_msg"));
        }
        if (this.a_verilog && this.verilogcode != null) {
            f.writeTextFile("reader" + ACMgenOptions.verilog_ext, this.verilogcode.getReader_v());
            f.writeTextFile("reader_engine" + ACMgenOptions.verilog_ext, this.verilogcode.getReaderE_v());
            f.writeTextFile("writer" + ACMgenOptions.verilog_ext, this.verilogcode.getWriter_v());
            f.writeTextFile("writer_engine" + ACMgenOptions.verilog_ext, this.verilogcode.getWriterE_v());
            f.writeTextFile("shmem" + ACMgenOptions.verilog_ext, this.verilogcode.getShMem_v());
            f.writeTextFile("ACM" + ACMgenOptions.verilog_ext, this.verilogcode.getACM_v());
            f.writeTextFile("mux" + ACMgenOptions.verilog_ext, this.verilogcode.getMux_v());
            f.copyFile(ACMgenOptions.flip_flop_v, "flip_flop" + ACMgenOptions.verilog_ext);
        } else if (this.a_cpp && this.cppcode == null) {
            this.printErrorMsg(ACMgen.msg.getMessage("cppcode_error_msg"));
        }
        if (this.a_pep && this.pncode != null) {
            f.writeTextFile(this.filebasename + ACMgenOptions.llnet_ext, this.pncode.getPEPcode());
        } else if (a_pep && pncode == null) {
            this.printErrorMsg(ACMgen.msg.getMessage("pncode_error_msg"));
        }
        if (this.a_petrify) {
            this.printErrorMsg("Petrify generator not implemented, yet");
        }
        if (this.a_smv && this.smvcode != null) {
            f.writeTextFile(this.filebasename + ACMgenOptions.smv_ext, this.smvcode.getCode());
        } else if (this.a_smv && this.smvcode == null) {
            this.printErrorMsg(ACMgen.msg.getMessage("smvcode_error_msg"));
        }
        if (this.a_smv_pn && this.smvpncode != null && this.pncode != null) {
            f.writeTextFile(this.filebasename + "_pn" + ACMgenOptions.smv_ext, this.smvpncode.getCode());
        } else if (this.a_smv_pn && (this.smvpncode == null || this.pncode == null)) {
            this.printErrorMsg(ACMgen.msg.getMessage("smvpncode_error_msg"));
        }
    }
