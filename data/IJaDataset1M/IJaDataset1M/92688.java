package spacewars.graficos.camadas;

import javax.media.opengl.GL;
import spacewars.camera.Camera;
import spacewars.graficos.EngineGrafico;
import spacewars.graficos.arquivos.swg.SWGManager;
import spacewars.graficos.texto.Escrevedor;
import spacewars.misc.Matriz;
import spacewars.principal.EngineJogo;

public class CamadaLogotipo extends CamadaPontoDeVista {

    static int dlSpaceWars;

    static int dlVader;

    static float angulo = 0;

    static float velocidadeAngular = new Float(EngineGrafico.ConfiguracoesGraficas.getProperty("VelocidadeAngular", "3.14"));

    ;

    static float alpha = 1f;

    static float velocidadeAlpha = 1f;

    static double lastTimeStamp = EngineGrafico.timeStamp;

    public CamadaLogotipo() {
        podeDesenharNulo = true;
        dlSpaceWars = SWGManager.defaultSWGManager.getObjeto(EngineJogo.class.getResource("/modelos/SWLogo/sw.swg"));
        dlVader = SWGManager.defaultSWGManager.getObjeto(EngineJogo.class.getResource("/modelos/vader/vader.swg"));
    }

    public void paintImpl() {
        desenhaUmaTela(0, 0, canvasWidth, canvasHeight);
    }

    public void desenhaUmaTela(int x, int y, int width, int height, Camera pontoDeVista) {
        if (pontoDeVista == null) desenhaUmaTela(x, y, width, height);
    }

    public void desenhaUmaTela(int x, int y, int width, int height) {
        gl.glClear(GL.GL_DEPTH_BUFFER_BIT);
        gl.glFrontFace(GL.GL_CCW);
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
        double tempoPassado = EngineGrafico.timeStamp - lastTimeStamp;
        lastTimeStamp = EngineGrafico.timeStamp;
        angulo += Math.min(tempoPassado, 0.2f) * velocidadeAngular;
        if (EngineJogo.estaPausado()) alpha += tempoPassado / velocidadeAlpha; else alpha -= tempoPassado / velocidadeAlpha;
        alpha = Math.max(0, Math.min(1, alpha));
        if (alpha == 0) return;
        setViewport(x, y, width, height);
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();
        projectionOrtho(-1, 1, 1, -1, -1, 1);
        gl.glDisable(GL.GL_LIGHTING);
        gl.glDisable(GL.GL_TEXTURE_2D);
        gl.glColor4f(0, 0, 0, alpha);
        gl.glBegin(GL.GL_QUADS);
        gl.glVertex2f(-1, -1);
        gl.glVertex2f(-1, 1);
        gl.glVertex2f(1, 1);
        gl.glVertex2f(1, -1);
        gl.glEnd();
        float lado = 0.2f;
        float alt = (float) height / width * lado;
        projectionFrustum(-lado, lado, -alt, alt, 0.1f, 10000f);
        gl.glMatrixMode(GL.GL_MODELVIEW);
        float pos = 1;
        gl.glLoadIdentity();
        glu.gluLookAt(Math.cos(angulo) * pos, -4, Math.sin(angulo) * pos, 0, 0, 0, 0, 0, 1);
        gl.glClear(GL.GL_DEPTH_BUFFER_BIT);
        gl.glColor4f(0.9f, 0.70f, 0.05f, alpha);
        gl.glCallList(dlSpaceWars);
        gl.glColor4f(1, 1, 1, alpha);
        desenhaDarthVader(x, y, width / 3, height / 3, angulo);
        desenhaDarthVader(x, y + height - height / 3, width / 3, height / 3, -angulo + Math.PI / 2);
        desenhaDarthVader(x + width - width / 3, y, width / 3, height / 3, angulo + Math.PI);
        desenhaDarthVader(x + width - width / 3, y + height - height / 3, width / 3, height / 3, -angulo + 3 * Math.PI / 2);
        setViewport(x, y, width, height);
        projectionOrtho(-500, 500, 100f * height / width, -900f * height / width, -10, 10);
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();
        java.awt.Font font = new java.awt.Font("CommercialScript BT", 0, 100);
        gl.glColor4f(1, 1, 0.5f, ((float) (Math.cos(angulo) + 1) / 4 + 0.5f) * alpha);
        gl.glDisable(GL.GL_LIGHTING);
        spacewars.graficos.texto.Escrevedor.escreveString(EngineJogo.messageStrings.getProperty("loading"), font, Escrevedor.CENTER);
    }

    protected void desenhaDarthVader(int x, int y, int width, int height, double ang) {
        setViewport(x, y, width, height);
        gl.glLoadIdentity();
        glu.gluLookAt(Math.sin(ang) * 10, -Math.cos(ang) * 10, 2, 0, 0, 0, 0, 0, 1);
        gl.glCallList(dlVader);
    }

    public void desenhaUmaTela(Matriz matrizCamera, float constTamanho) {
    }
}
