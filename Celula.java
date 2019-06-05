package juegodelavida;
import java.awt.Color;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
public class Celula {
    Celula(Celula otro){
        c = otro.getColor();
        pos = otro.getPos();
        rect = otro.getRect();
    }
    Celula(Color c, float w, float h, Point2D.Float pos){
        this.c = c;
        this.pos = pos;
        rect = new Rectangle2D.Float(pos.x, pos.y, w, h);
    }
    public boolean contains(Point2D.Float p){
        return (p.y < pos.y + rect.height && p.y > pos.y) && (p.x < pos.x + rect.width && p.x > pos.x);
    }
    public Color getColor(){return c;}
    public void setColor(Color c){ this.c = c;}
    public Rectangle2D.Float getRect(){return rect;}
    public Point2D.Float getPos(){return pos;}
    private Color c;
    public boolean viva = false;
    private Rectangle2D.Float rect;
    private Point2D.Float pos;
}

