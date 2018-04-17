package sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import help.Info;


public class Paddle extends Sprite {
    private Vector2 positionM;
    private Vector2 speed;
    private Rectangle bounds;
    private float resize;
    private int numeroGiocatori;
    private int giocatore;

    public Paddle(float resize, int numeroGiocatori, int giocatore){
        super(new Texture("mattonalla curva.png"));
        this.numeroGiocatori=numeroGiocatori;
        this.giocatore=giocatore;
        positionM = new Vector2((Info.larghezza/numeroGiocatori)*(giocatore-1), 0);
        speed = new Vector2(0,0);
        this.resize = resize;
        bounds = new Rectangle(positionM.x, positionM.y, Paddle.this.getWidth() * Info.paddleresize, Paddle.this.getHeight() *Info.paddleresize );
        //il bound si riferiscono al rettangolo invisibile che costruirò intorno alla texture
        // l'altezza e la larghezza sono della texture
    }


    public Vector2 getPosition() {
        return positionM;
    }

    public Vector2 getSpeed() { return speed; }

    public Rectangle getBounds() { return bounds; }
}
