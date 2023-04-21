        public void updateOptionCount(SplitNode source, HoeffdingOptionTree hot) {
            if (this.optionCount == -999) {
                this.parent.updateOptionCount(source, hot);
            } else {
                int maxChildCount = -999;
                SplitNode curr = this;
                while (curr != null) {
                    for (Node child : curr.children) {
                        if (child instanceof SplitNode) {
                            SplitNode splitChild = (SplitNode) child;
                            if (splitChild.optionCount > maxChildCount) {
                                maxChildCount = splitChild.optionCount;
                            }
                        }
                    }
                    if ((curr.nextOption != null) && (curr.nextOption instanceof SplitNode)) {
                        curr = (SplitNode) curr.nextOption;
                    } else {
                        curr = null;
                    }
                }
                if (maxChildCount > this.optionCount) {
                    int delta = maxChildCount - this.optionCount;
                    this.optionCount = maxChildCount;
                    if (this.optionCount >= hot.maxOptionPathsOption.getValue()) {
                        killOptionLeaf(hot);
                    }
                    curr = this;
                    while (curr != null) {
                        for (Node child : curr.children) {
                            if (child instanceof SplitNode) {
                                SplitNode splitChild = (SplitNode) child;
                                if (splitChild != source) {
                                    splitChild.updateOptionCountBelow(delta, hot);
                                }
                            }
                        }
                        if ((curr.nextOption != null) && (curr.nextOption instanceof SplitNode)) {
                            curr = (SplitNode) curr.nextOption;
                        } else {
                            curr = null;
                        }
                    }
                    if (this.parent != null) {
                        this.parent.updateOptionCount(this, hot);
                    }
                }
            }
        }
