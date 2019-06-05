package juegodelavida;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Panel extends JPanel implements Runnable{
    private Celula matrix[][];
    private Celula aux[][];
    private final int filas = 40, columnas = 70;
    private Thread hilo;
    private JLabel generacion;
    private JButton reiniciar, random;
    private boolean flag, detener;
    private int cGen;
    private float gros = 1f;
    public Panel(){
        float w_ = 15;
        float h_ = 15;
        matrix = new Celula[filas][columnas];
        aux = new Celula[filas][columnas];
        cGen = 0;
        flag = true;
        generacion = new JLabel("Generación: "+ cGen);
        generacion.setBounds(20, 20, 1280/2, 10);
        for(int i = 0; i < filas; i++){
            for(int j = 0; j < columnas; j++){
                matrix[i][j] = new Celula(Color.blue, w_, h_, new Point2D.Float(20 + j*(w_ + gros), 40 + i*(h_ + gros))); 
                aux[i][j] = new Celula(matrix[i][j]);
            }
        }
        hilo = new Thread(this);
        hilo.setName("LE MATRIX");
        reiniciar = new JButton("Reiniciar");
        random = new JButton("Random");
        random.setFocusable(false);
        random.setBounds(1160, 720/2 - 30, 100, 20);
        detener = false;
        this.setVisible(true);
        this.setLayout(null);
        reiniciar.setBounds(1160, 720/2, 100, 20);
        reiniciar.setFocusable(false);
        ManejaMouse m = new ManejaMouse();
        reiniciar.addMouseListener(m);
        random.addMouseListener(m);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        this.setFocusable(true);
        this.add(generacion);
        this.add(reiniciar);
        this.add(random);
        this.addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    if(!hilo.isAlive()){
                        executor.submit(hilo);
                    }
                    flag = false;
                    detener = true;
                }else if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
                    detener = false;
                }
            }
        });
        this.addMouseListener(m);
    }
    private int count(int f, int c){
        int vivos = 0;
        for(int i = f-1; i <= f+1; i++){
            for(int j = c-1; j <= c+1; j++){
                if(j < 0 || j >= columnas || i < 0 || i >= filas) continue;
                if(i == f && j == c) continue;
                if(matrix[i][j].viva == true) vivos++;
            }
        }
        return vivos;
    }
    @Override
    public void run(){
        while(detener){
            int vivos;
            try{
                Thread.sleep(150);
            }catch(Exception e){ System.out.println(e);
            }
            for(int i = 0; i < filas; i++){
                for(int j = 0; j < columnas; j++){
                    vivos = count(i, j);
                    if(matrix[i][j].viva == true){
                        if(!(vivos == 3 || vivos == 2)){
                            aux[i][j].setColor(Color.blue);
                            aux[i][j].viva = false;
                        }else{
                            aux[i][j].setColor(Color.red);
                            aux[i][j].viva = true;
                        }
                    }else{
                        if(vivos == 3){
                            aux[i][j].setColor(Color.red);
                            aux[i][j].viva = true;
                        }else{
                            aux[i][j].setColor(Color.blue);
                            aux[i][j].viva = false;
                        }
                    }
                }
            }
            cGen++;
            generacion.setText("Generación: "+cGen);
            for(int i = 0; i < filas; i++){
                for(int j = 0; j < columnas; j++){
                    matrix[i][j].setColor(aux[i][j].getColor());
                    matrix[i][j].viva = aux[i][j].viva;
                }
            }
            repaint();
        }
    }
    @Override
    public void update(Graphics g){
       paint(g);
    }
    
    @Override
    public void paint(Graphics g){
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(3.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
        g2d.setColor(Color.black);
        for(int i = 0; i < filas; i++){
            for(int j = 0; j < columnas; j++){
                g2d.setColor(matrix[i][j].getColor());
                g2d.fill(matrix[i][j].getRect());
                
            }
        }
    }
    class ManejaMouse extends MouseAdapter{
        @Override
        public void mousePressed(MouseEvent e){
            if(e.getSource().equals(random) && !detener){
              int k = 700, f, c;
              for(int i = 0; i < k; i++){
                  f = (int)Math.floor(Math.random()*(filas));
                  c = (int)Math.floor(Math.random()*(columnas));
                  matrix[f][c].setColor(Color.red);
                  matrix[f][c].viva = true;
                }
              repaint();
            }
            if(e.getSource().equals(reiniciar) && !detener){
                flag = true;
                for(int i = 0; i < filas; i++){
                    for(int j = 0; j < columnas; j++){
                        if(matrix[i][j].viva){
                            matrix[i][j].setColor(Color.blue);
                            matrix[i][j].viva = false;
                        }
                        if(aux[i][j].viva){
                            aux[i][j].setColor(Color.blue);
                            aux[i][j].viva = false;
                        }
                    }
                }
                repaint();
                generacion.setText("Generación: 0");
                cGen = 0;
            }
            else if(flag){
                if(e.getButton() == MouseEvent.BUTTON1){
                    for(int i = 0; i < filas; i++){
                        for(int j = 0; j < columnas; j++){
                            float x = e.getPoint().x;
                            float y = e.getPoint().y;
                            if(matrix[i][j].contains(new Point2D.Float(x, y))){
                                matrix[i][j].setColor(Color.red);
                                matrix[i][j].viva = true;
                                repaint();
                                break;
                            }
                        }
                    }
                }
            }
        }   
    } 
}
