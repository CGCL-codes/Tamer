        @Override
        public String toString() {
            return app.getComponentLabel(index) + (breakOnRead ? ": read" : ": write");
        }
