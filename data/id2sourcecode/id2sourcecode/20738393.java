    public void dTestDataStructures() {
        int i;
        printf("testDynamicsStuff()\n");
        DxBody[] body = new DxBody[NUM];
        int nb = 0;
        DxJoint[] joint = new DxJoint[NUM];
        int nj = 0;
        for (i = 0; i < NUM; i++) body[i] = null;
        for (i = 0; i < NUM; i++) joint[i] = null;
        printf("creating world\n");
        DxWorld w = DxWorld.dWorldCreate();
        checkWorld(w);
        for (; ; ) {
            if (nb < NUM && dRandReal() > 0.5) {
                printf("creating body\n");
                body[nb] = DxBody.dBodyCreate(w);
                printf("\t--> %p\n", body[nb].toString());
                nb++;
                checkWorld(w);
                printf("%d BODIES, %d JOINTS\n", nb, nj);
            }
            if (nj < NUM && nb > 2 && dRandReal() > 0.5) {
                DxBody b1 = (DxBody) body[(int) (dRand() % nb)];
                DxBody b2 = (DxBody) body[(int) (dRand() % nb)];
                if (b1 != b2) {
                    printf("creating joint, attaching to %p,%p\n", b1, b2);
                    joint[nj] = dJointCreateBall(w, null);
                    printf("\t-->%p\n", joint[nj]);
                    checkWorld(w);
                    joint[nj].dJointAttach(b1, b2);
                    nj++;
                    checkWorld(w);
                    printf("%d BODIES, %d JOINTS\n", nb, nj);
                }
            }
            if (nj > 0 && nb > 2 && dRandReal() > 0.5) {
                DxBody b1 = body[(int) (dRand() % nb)];
                DxBody b2 = body[(int) (dRand() % nb)];
                if (b1 != b2) {
                    int k = (int) (dRand() % nj);
                    printf("reattaching joint %p\n", joint[k]);
                    joint[k].dJointAttach(b1, b2);
                    checkWorld(w);
                    printf("%d BODIES, %d JOINTS\n", nb, nj);
                }
            }
            if (nb > 0 && dRandReal() > 0.5) {
                int k = (int) (dRand() % nb);
                printf("destroying body %p\n", body[k]);
                body[k].dBodyDestroy();
                checkWorld(w);
                for (; k < (NUM - 1); k++) body[k] = body[k + 1];
                nb--;
                printf("%d BODIES, %d JOINTS\n", nb, nj);
            }
            if (nj > 0 && dRandReal() > 0.5) {
                int k = (int) (dRand() % nj);
                printf("destroying joint %p\n", joint[k]);
                dJointDestroy(joint[k]);
                checkWorld(w);
                for (; k < (NUM - 1); k++) joint[k] = joint[k + 1];
                nj--;
                printf("%d BODIES, %d JOINTS\n", nb, nj);
            }
        }
    }
