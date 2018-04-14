package com.mygdx.game;
import ClientServer.ChatClient;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import help.GameState;
import help.Info;
import sprites.*;



import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Input.TextInputListener;

import java.util.ArrayList;

public class MyGdxGame extends Game implements TextInputListener {
    private Collision col;
    private SpriteBatch batch;
    private Brick Brick;
    private ArrayList<Brick> mattoncini = new ArrayList();
    private Ball palla;
    private Paddle Paddle;
    private Texture bg;
    private Texture start;
    private Texture gameOver;
    private Texture menu;
    private Texture youWin;
    private GameState gameState;
    private CommandPlayer player1;
    private Texture multiplayerButton;
    private Texture resumeButton;
    private Texture playButton;
    private Texture exitButton;
    private Texture menuButton;
    private  Livello livello = new Livello(Brick, palla);
    private boolean nextLevel;
    private ArrayList<Integer> indici;
    private ArrayList<Brick> mattoncini1;
    private  int matEliminati;

    Disegnare disegna= new Disegnare();

    Music music ;
    Music music2 ;
    private boolean loser;
    BitmapFont bitmapFont ;
    int LostLives =0;
    String text;
    TextInputListener textInputListener;
	ChatClient chatClient = new ChatClient() ;
	@Override
	public void create () {
        batch = new SpriteBatch();

        bitmapFont = new BitmapFont();
        bitmapFont.setColor(Color.WHITE);
        bitmapFont.getData().setScale(1.2f);
        music =  Gdx.audio.newMusic(Gdx.files.internal("music.mp3"));
        music2 =  Gdx.audio.newMusic(Gdx.files.internal("Untitled.mp3"));


        reset();

        start=new Texture("start.jpg");
        gameOver=new Texture("gameover.jpeg");
        youWin=new Texture("nextlevel.jpg");
        playButton = new Texture("play.png");
        exitButton = new Texture("exit.png");
        menu = new Texture("menuscreen.jpg");
        menuButton = new Texture("menu.png");
        resumeButton = new Texture("resume.png");
        multiplayerButton = new Texture("multiplayer.png");



        gameState=GameState.MENU;
        nextLevel=false;
       // Gdx.input.getTextInput(textInputListener, "Title", "Default text", "OK");
       // Gdx.app.log("Text", "test");


    }

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		music.setVolume(1);
        music.play();

        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE))
        {
            if(gameState.equals(GameState.GAME_OVER))
                Gdx.app.exit();
        }
		if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) { //Barra spaziatrice per iniziare
		    if(gameState.equals(GameState.INIT)) {
		        gameState=GameState.ACTION;
		        music2.pause();
            }
            if(gameState.equals(GameState.YOU_WON)) {
		        reset();
		        nextLevel=false;
		        gameState=GameState.ACTION;
            }
            if(gameState.equals(GameState.GAME_OVER)) {
                reset();
                gameState=GameState.ACTION;
            }
        }
      //  chatClient.start_main(palla.getPositionBall());
        if(nextLevel) {//deve stare dentro render perchè deve essere controllato sempre
            mattoncini = livello.selectLv();  //ritorno l'array adatto al nuovo livello
            bg = livello.getBg(); //reimposto il bg

        }

        drawScene();

    }

    public void drawScene() {
        batch.begin();


        if(gameState == GameState.PAUSE){
            batch.draw(menu,0,0);
            batch.draw(resumeButton, Info.larghezza/2 - playButton.getWidth()/2, 550);      // al posto di metterli cosi posso usare delle costanti
            batch.draw(exitButton, Info.larghezza/2 - exitButton.getWidth()/2,150);
            batch.draw(menuButton, Info.larghezza/2 - multiplayerButton.getWidth()/2, 350);
            System.out.println(Gdx.input.getY() + " " + Gdx.input.getX() );

            if(Gdx.input.getX()>Info.larghezza/2 - playButton.getWidth()/2    && (Gdx.input.getX() <Info.larghezza/2 + exitButton.getWidth()/2)  && (Info.altezza - Gdx.input.getY() > 150 && (Info.altezza - Gdx.input.getY() < 150 + exitButton.getHeight() ))){
                if(Gdx.input.isTouched())
                    Gdx.app.exit();
            }
            if(Gdx.input.getX()>Info.larghezza/2 - resumeButton.getWidth()/2    && (Gdx.input.getX() <Info.larghezza/2 + resumeButton.getWidth()/2)  && (Info.altezza - Gdx.input.getY() > 550 && (Info.altezza - Gdx.input.getY() < 550 + resumeButton.getHeight() ))){
                if(Gdx.input.isTouched())
                    gameState = GameState.ACTION;
            }
            if(Gdx.input.getX()>Info.larghezza/2 - menuButton.getWidth()/2    && (Gdx.input.getX() <Info.larghezza/2 + menuButton.getWidth()/2)  && (Info.altezza - Gdx.input.getY() > 350 && (Info.altezza - Gdx.input.getY() < 350 + menuButton.getHeight() ))){
                if(Gdx.input.isTouched()) {
                    gameState = GameState.MENU;
                    reset();

                }
            }
        }

        if(gameState == GameState.MENU){
            Gdx.gl.glClearColor(1, 0, 0, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            batch.draw(menu,0,0);
            batch.draw(playButton, Info.larghezza/2 - playButton.getWidth()/2, 550);      // al posto di metterli cosi posso usare delle costanti
            batch.draw(exitButton, Info.larghezza/2 - exitButton.getWidth()/2,150);
            batch.draw(multiplayerButton, Info.larghezza/2 - multiplayerButton.getWidth()/2, 350);//immagini bruttissime
            if(Gdx.input.getX()>Info.larghezza/2 - playButton.getWidth()/2    && (Gdx.input.getX() <Info.larghezza/2 + exitButton.getWidth()/2)  && (Info.altezza - Gdx.input.getY() > 150 && (Info.altezza - Gdx.input.getY() < 150 + exitButton.getHeight() ))){
                if(Gdx.input.isTouched())
                    Gdx.app.exit();
            }
            if(Gdx.input.getX()>Info.larghezza/2 - playButton.getWidth()/2    && (Gdx.input.getX() <Info.larghezza/2 + playButton.getWidth()/2)  && (Info.altezza - Gdx.input.getY() > 550 && (Info.altezza - Gdx.input.getY() < 550 + exitButton.getHeight() ))){
                if(Gdx.input.isTouched())
                    gameState = GameState.ACTION;
            }
            if(Gdx.input.getX()>Info.larghezza/2 - multiplayerButton.getWidth()/2    && (Gdx.input.getX() <Info.larghezza/2 + multiplayerButton.getWidth()/2)  && (Info.altezza - Gdx.input.getY() > 350 && (Info.altezza - Gdx.input.getY() < 350 + multiplayerButton.getHeight() ))){
                if(Gdx.input.isTouched())
                    gameState = GameState.MULTIPLAYER;
            }
        }


        /*
        QUA ALBI PER AGGIUNGERE IL MULTIPLAYER USO LO STATO MULTIPLAYER COSI QUANDO SCHIACCI DAL MENU SU MULTIPLAYER VA IN QUELLO STATO QUINDI si puo fare tipo

        if(gameState == GameState.MULTIPLAYER){

            qua la implementazione del multiplayer

        }
         */

        if(gameState.equals(GameState.ACTION) ) {

            palla.getPositionBall().add(palla.getSpeedBall().x * Info.dt, palla.getSpeedBall().y* Info.dt);
            palla.getBoundsBall().setPosition(palla.getPositionBall().x, palla.getPositionBall().y);
            
            disegna.disegnare(batch, mattoncini, Paddle, palla, bg);
            bitmapFont.draw(batch, "You lost: "+String.valueOf(LostLives) + " times", 20, 830);

            player1.Move();     //mi permette di muovere il giocatore
            if(player1.checkpause()){
                gameState = GameState.PAUSE;
            }

            gestisciCollisioni();

            System.out.println(matEliminati+ " = " + livello.nMatMorbidi);

            if(matEliminati == livello.nMatMorbidi) {
                gameState=GameState.YOU_WON;
                livello.inceaseLv();
            }

            if(palla.getPositionBall().y<=0) {
                gameState=GameState.GAME_OVER;
                livello.setnMatMorbidi(0);
                livello.setnMat(0);
                matEliminati = 0;
                LostLives ++;
                music2.play();
                palla.setPositionBall();
            }
        }
        else {
            if(gameState.equals(GameState.INIT)) {
                batch.draw(start,0,0);

            }
            if(gameState.equals(GameState.YOU_WON)) {
                nextLevel=true;
                livello.setnMatMorbidi(0);
                livello.setnMat(0);
                matEliminati = 0;
                batch.draw(youWin,0,0);
            }
            if(gameState.equals(GameState.GAME_OVER) ) {
                batch.draw(gameOver,0,0);
                music.pause();
            }
        }
        batch.end();
    }

	
	@Override
	public void dispose () {
		batch.dispose();
	}

	public void reset() {
        palla = new Ball();
        Paddle = new Paddle(0.5f);
        player1 = new CommandPlayer(Paddle);     //istanzio un Commandplayer( posso averne diversi per ogni player
        mattoncini = livello.selectLv(); //la classe livello si occuperà di ritornare l'array list dei mattoncini adatti a questo livello
        bg =livello.getBg();
    }

    public void gestisciCollisioni() {
        float oldSpeedBallX=palla.getSpeedBall().x;
        float oldSpeedBallY=palla.getSpeedBall().y;

        indici=new ArrayList<Integer>();
        for (Brick brick : mattoncini) {
            //dato che ho un arraylist devo aggiornare le condizioni dei mattoncini dentro un ciclo for
            col = new Collision(brick, palla);
            if (col.check()) {
                indici.add(mattoncini.indexOf(brick));
            }
        }

        col.checkside(Paddle);

        if (!indici.isEmpty()) {
            if(indici.size()>=2) {
                ArrayList<Brick> tempMatt=new ArrayList<sprites.Brick>();
                palla.setSpeedBall(new Vector2(oldSpeedBallX,-oldSpeedBallY));
                for(int i:indici) {
                    if(mattoncini.get((int)indici.get(0)).getDurezza() == 0) {//i mattoncini vengono eliminati solo se sono quelli MORBIDI
                        tempMatt.add(mattoncini.get(i));
                        matEliminati ++;
                    }
                }
                for(Brick brick:tempMatt) {
                    mattoncini.remove(brick);
                }
            }
            else {
                if(mattoncini.get((int)indici.get(0)).getDurezza() == 0) { //se i mattoncini sono mattoncini "morbidi" li posso eliminare
                    mattoncini.remove((int) indici.get(0));
                    matEliminati++;
                }
            }
        }
    }

    @Override
    public void input(String text) {

    }

    @Override
    public void canceled() {

    }
}
