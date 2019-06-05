package juegodelavida;
import java.awt.Toolkit;
import javax.swing.JFrame;

public class JuegoDeLaVida extends JFrame{
    public JuegoDeLaVida(int w, int h){
        super("Juego de la vida");
        this.setBounds((Toolkit.getDefaultToolkit().getScreenSize().width-w)/2, (Toolkit.getDefaultToolkit().getScreenSize().height-h)/2, w, h);
        this.setVisible(true);
        this.setFocusable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.add(new Panel());
        
    }
}
