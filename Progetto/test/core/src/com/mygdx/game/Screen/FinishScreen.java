package com.mygdx.game.Screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Scaling;
import com.mygdx.game.BreakGame;

/**
 * @author ligato,schillaci, regna
 *
 * Gestisce la schermata di fine gioco
 */

public class FinishScreen implements Screen {

    BreakGame game;
    private Texture win;
    private int newHeight, newWight;

    /**
     *
     * @param game oggetto Breack game
     */
    public FinishScreen(BreakGame game) {
        this.game = game;
    }


    /**
     * quando vinci il gioco asegna la nuova texture che dovrà essere disegnata
     */

    @Override
    public void show() {
        win = new Texture("nextlevel.jpg");

    }

    /**
     * Renderizza il back e disegna la texture che ti avvisa di aver vinto il gioco
     * @param delta
     */
    @Override
    public void render(float delta) {
        game.getBatch().begin();

        game.getBatch().draw(win, 0, 0);

        if(Gdx.input.isKeyPressed(Input.Keys.ENTER))
            dispose();
            game.setScreen(new MainMenuScreen(game));

        game.getBatch().end();
    }

    /**
     * Ci permetta di ridimensionare la finestra
     *
     * @param width larghezza della finestra
     * @param height altezza della finestra
     */
    @Override
    public void resize(int width, int height) {

        this.newHeight = height;
        this.newWight = width;


        Vector2 size = Scaling.fit.apply(800, 850, width, height);
        int viewportX = (int)(width - size.x) / 2;
        int viewportY = (int)(height - size.y) / 2;
        int viewportWidth = (int)size.x;
        int viewportHeight = (int)size.y;
        Gdx.gl.glViewport(viewportX, viewportY, viewportWidth, viewportHeight);

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
