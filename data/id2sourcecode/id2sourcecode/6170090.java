    @Override
    public String toString() {
        return "file_state[" + super.toString() + ", filepath=" + getFilepath() + ", path=" + getPath() + ", filename=" + getFilename() + ", type=" + getType() + ", group_id=" + getGroupID() + ", user_id=" + getUserID() + ", a_time=" + getATime() + ", c_time=" + getCTime() + ", m_time=" + getMTime() + ", size=" + getSize() + ", suid=" + getSuID() + ", sgid=" + getSgID() + ", sticky=" + getSticky() + ", uread=" + getURead() + ", uwrite=" + getUWrite() + ", uexec=" + getUExec() + ", gread=" + getGRead() + ", gwrite=" + getGWrite() + ", gexec=" + getGExec() + ", oread=" + getORead() + ", owrite=" + getOWrite() + ", oexec=" + getOExec() + ", has_extended_acl=" + getHasExtendedAcl() + "]";
    }
