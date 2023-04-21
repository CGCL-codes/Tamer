    public void dump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {
        writer.print(prefix);
        writer.print("mName=");
        writer.print(mName);
        writer.print(" mIndex=");
        writer.print(mIndex);
        writer.print(" mCommitted=");
        writer.println(mCommitted);
        if (mTransition != FragmentTransaction.TRANSIT_NONE) {
            writer.print(prefix);
            writer.print("mTransition=#");
            writer.print(Integer.toHexString(mTransition));
            writer.print(" mTransitionStyle=#");
            writer.println(Integer.toHexString(mTransitionStyle));
        }
        if (mEnterAnim != 0 || mExitAnim != 0) {
            writer.print(prefix);
            writer.print("mEnterAnim=#");
            writer.print(Integer.toHexString(mEnterAnim));
            writer.print(" mExitAnim=#");
            writer.println(Integer.toHexString(mExitAnim));
        }
        if (mBreadCrumbTitleRes != 0 || mBreadCrumbTitleText != null) {
            writer.print(prefix);
            writer.print("mBreadCrumbTitleRes=#");
            writer.print(Integer.toHexString(mBreadCrumbTitleRes));
            writer.print(" mBreadCrumbTitleText=");
            writer.println(mBreadCrumbTitleText);
        }
        if (mBreadCrumbShortTitleRes != 0 || mBreadCrumbShortTitleText != null) {
            writer.print(prefix);
            writer.print("mBreadCrumbShortTitleRes=#");
            writer.print(Integer.toHexString(mBreadCrumbShortTitleRes));
            writer.print(" mBreadCrumbShortTitleText=");
            writer.println(mBreadCrumbShortTitleText);
        }
        if (mHead != null) {
            writer.print(prefix);
            writer.println("Operations:");
            String innerPrefix = prefix + "    ";
            Op op = mHead;
            int num = 0;
            while (op != null) {
                writer.print(prefix);
                writer.print("  Op #");
                writer.print(num);
                writer.println(":");
                writer.print(innerPrefix);
                writer.print("cmd=");
                writer.print(op.cmd);
                writer.print(" fragment=");
                writer.println(op.fragment);
                if (op.enterAnim != 0 || op.exitAnim != 0) {
                    writer.print(prefix);
                    writer.print("enterAnim=");
                    writer.print(op.enterAnim);
                    writer.print(" exitAnim=");
                    writer.println(op.exitAnim);
                }
                if (op.removed != null && op.removed.size() > 0) {
                    for (int i = 0; i < op.removed.size(); i++) {
                        writer.print(innerPrefix);
                        if (op.removed.size() == 1) {
                            writer.print("Removed: ");
                        } else {
                            writer.println("Removed:");
                            writer.print(innerPrefix);
                            writer.print("  #");
                            writer.print(num);
                            writer.print(": ");
                        }
                        writer.println(op.removed.get(i));
                    }
                }
                op = op.next;
            }
        }
    }
