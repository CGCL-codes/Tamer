    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("FlowWindow size=").append(length);
        sb.append(" full=").append(isFull).append(" empty=").append(isEmpty);
        sb.append(" readPos=").append(readPos).append(" writePos=").append(writePos);
        sb.append(" consumed=").append(consumed).append(" produced=").append(produced);
        return sb.toString();
    }
