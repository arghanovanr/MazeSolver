
package MazeSolver;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.LinkedList;
import java.util.Stack;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JRadioButton;
import javax.swing.SwingUtilities;

public class View extends JFrame implements ActionListener {

// Semua Variable Pendukung=====================================================
MouseHandler mHandeler = new MouseHandler();
LinkedList<Integer> XCoor = new LinkedList<Integer>();
LinkedList<Integer> YCoor = new LinkedList<Integer>();
Stack<Integer> XCoorStack = new Stack<Integer>();
Stack<Integer> YCoorStack = new Stack<Integer>();
private JButton run;
private boolean TargetFoundGlobal = false;
private int XShort;
private int YShort;
private int XTarget;
private int YTarget;
private JRadioButton path ;
private JRadioButton obstacle;
private JRadioButton start;
private JRadioButton target;
private ButtonGroup group;

private int [][] maze = new int [8][8];
  // 0 = putih (kotak yang belum dikunjungi)
  // 1 = merah (kotak penghalang)
  // 2 = biru  (player)
  // 3 = hijau (jalan)
  // 4 = cyan  (shortest path)
  // 9 = kuning (target) 



//Tampilan saat koder dijalankan================================================
public View() {
    // Inisialisasi nilai maze
    for (int i = 0; i < maze.length; i++) {
        for (int j = 0; j < maze[0].length; j++) {
            maze[i][j] = 1;
        }
    }
    
    run = new JButton("NEXT STEP");
    run.addActionListener(this);
    setLayout(new FlowLayout(50,20,20));
    addMouseListener(mHandeler);
    setTitle("MAZE SOLVER ");
    setSize(640,480);
    path = new JRadioButton("PATH",true);
    obstacle = new JRadioButton("OBSTACLE",false);
    start = new JRadioButton("START",false);
    target = new JRadioButton("TARGET",false);
    add(path);
    add(obstacle);
    add(start);
    add(target);
    add(run);
    group = new ButtonGroup();
    group.add(path);
    group.add(obstacle);
    group.add(start);
    group.add(target);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
}

//Untuk Mendeteksi tetangga dari suatu grid=====================================
public void NeighboorPath (int x,int y)
{
    if(y+1 < maze.length)
    {
        if(maze[y+1][x]==0)
        {
            maze[y+1][x]=3;
            
            YCoor.add(y+1);
            XCoor.add(x);
            YCoorStack.add(y+1);
            XCoorStack.add(x);
        }
    }
    
    if(y-1 >= 0)
    {
        if(maze[y-1][x]==0)
        {
            maze[y-1][x]=3;
            
            YCoor.add(y-1);
            XCoor.add(x);
            YCoorStack.add(y-1);
            XCoorStack.add(x);
        }
    }
    if(x+1 < maze[0].length)
    {
        if(maze[y][x+1]==0)
        {
            maze[y][x+1]=3;
            
            YCoor.add(y);
            XCoor.add(x+1);
            YCoorStack.add(y);
            XCoorStack.add(x+1);
        }
    }
    if(x-1 >= 0)
    {
        if(maze[y][x-1]==0)
        {
            maze[y][x-1]=3;
            
            YCoor.add(y);
            XCoor.add(x-1);
            YCoorStack.add(y);
            XCoorStack.add(x-1);
        }
    }
}

//Untuk mengetahui target sudah ditemukan atau tidak============================
public boolean Searching(int x,int y)
{
    boolean TargetFound = false;
    if(y+1 < maze.length && !TargetFound)
    {
        if(maze[y+1][x]==9)
        {
            TargetFound = true;
            
            YCoorStack.add(y+1);
            YShort = y+1;
            YTarget = y+1;
            XCoorStack.add(x);
            XShort = x;
            XTarget = x;
        }
    }
    
    if(y-1 >= 0 && !TargetFound)
    {
        if(maze[y-1][x]==9)
        {
            TargetFound = true;
            YCoorStack.add(y-1);
            YShort = y-1;
            YTarget = y-1;
            XCoorStack.add(x);
            XShort = x;
            XTarget = x;
        }
    }
    if(x+1 < maze[0].length && !TargetFound )
    {
        if(maze[y][x+1]==9)
        {
            TargetFound = true;
            YCoorStack.add(y);
            YShort = y;
            YTarget = y;
            XCoorStack.add(x+1);
            XShort = x+1;
            XTarget = x+1;
        }
    }
    if(x-1 >= 0 && !TargetFound )
    {
        if(maze[y][x-1]==9)
        {
            TargetFound = true;
            YCoorStack.add(y);
            YShort = y;
            YTarget = y;
            XCoorStack.add(x-1);
            XShort = x-1;
            XTarget = x-1;
        }
    }
    if(TargetFound){TargetFoundGlobal = true;}
    return TargetFound;
}

//Coding yang berjalan jika tombol RUN di klik==================================
public void actionPerformed(ActionEvent c)
{
    if(c.getSource() == run)
    {
        if(!XCoor.isEmpty())
        {
            int x = XCoor.pop();
            int y = YCoor.pop();
            if (Searching(x,y))
            {
                System.out.println("Target Sudah ditemukan");
            }
             else if (!TargetFoundGlobal)
            {
                NeighboorPath(x,y);
            }  
        }
        else if (!TargetFoundGlobal){System.out.println("Tidak Ketemu");}
        if(TargetFoundGlobal)
        {
            if (!XCoorStack.isEmpty())
            {
                TargetConstant();
                int x = XCoorStack.pop();   
                int y = YCoorStack.pop();
                if(ShortestPath(x,y))
                {
                    XShort = x;
                    YShort = y;
                }
            }
            else System.out.println("Jalan Terpendek Sudah Dibuat");
        };
        repaint();
    }

}

//Untuk Mencari Jalur Terpendek=================================================
public boolean ShortestPath(int x,int y)
{
    if(x-1 == XShort && y == YShort)
    {
        maze[y][x-1] = 4;
        return true;
    }
    else if(x+1 == XShort && y == YShort)
    {
        maze[y][x+1] = 4;
        return true;
    }
    else if(x == XShort && y-1 == YShort)
    {
        maze[y-1][x] = 4;
        return true;
    }
    else if(x == XShort && y+1 == YShort)
    {
        maze[y+1][x] = 4;
        return true;
    }
    else 
    {
        return false;
    }
};

//Untuk Membuat Target tetap bernilai 9(warna kuning)===========================
public void TargetConstant()
{
    maze[YTarget][XTarget]=9;
    repaint();
}

//Pendeteksi klik pada pilihan edit warna
public int EditSettingsWhenClick()     
{
   int EditSettings = 0;
   if(path.isSelected())
       {
           EditSettings = 0;
       }
       else if(obstacle.isSelected())
       {
           EditSettings = 1;
       }
       else if(start.isSelected())
       {
           EditSettings = 2;
      }
       else if(target.isSelected())
       {
           EditSettings = 9;
       }
   return EditSettings;
}

//Pendeteksi klik mouse=========================================================
public class MouseHandler implements MouseListener{
    
    //Coding yang jalan ketika mouse di klik di dalam layar=====================
    @Override
    public void mouseClicked(MouseEvent e ){
    
    //============agar mouse click tetap di dalam grid============
    if( e.getX() > 100 && 
        e.getY() > 100 && 
        e.getX() < (100+(maze[0].length*30)) && 
        e.getY() < (100+(maze.length*30)))
    //=============================================================
        {
            int x = 100;
            int y = 100;    
            x = e.getX() ; y = e.getY();
            int setting = EditSettingsWhenClick();
                if(setting == 0)
                {
                    x = (x - 100)/30;
                    y = (y - 100)/30;
                    maze[y][x] = setting;
                }
                else if(setting == 1)
                {
                    x = (x - 100)/30;
                    y = (y - 100)/30;
                    maze[y][x] = setting;
                }
                else if(setting == 2)
                {
                    x = (x - 100)/30;
                    y = (y - 100)/30;
                    // grid unik agar start selalu 1 grid
                    for (int i = 0; i < maze.length; i++) {
                        for (int j = 0; j < maze[0].length; j++) {
                            if(maze[i][j]==setting) maze[i][j]=0;
                        }
                    }
                    maze[y][x] = setting;
                    XCoor.add(0, x);
                    YCoor.add(0, y);
                    XCoorStack.add(0, x);
                    YCoorStack.add(0, y);
                }
                else if(setting == 9)
                {
                    x = (x - 100)/30;
                    y = (y - 100)/30;
                    // grid unik agar target selalu 1 grid
                    for (int i = 0; i < maze.length; i++) {
                        for (int j = 0; j < maze[0].length; j++) {
                            if(maze[i][j]==setting) maze[i][j]=0;
                        }
                    }
                    maze[y][x] = setting;
                }
            repaint();
        }
    }
  
    
 
    
    @Override
    public void mousePressed(MouseEvent e){
    
    }
    
    @Override
    public void mouseReleased (MouseEvent e){
    
    }
    
    @Override
    public void mouseEntered (MouseEvent e){
    
    }
    
    @Override
    public void mouseExited (MouseEvent e){
    
    }
}





//Proses penggambaran grid di layar=============================================
@Override
public void paint (Graphics g){
    super.paint(g);
    
    g.translate(100, 100);
    for (int baris = 0; baris < maze.length ; baris++){
        for (int kolom = 0; kolom < maze[0].length; kolom++) {
            Color color;
            switch (maze[baris][kolom]){
                case 1: color = Color.RED;break;
                case 2: color = Color.BLUE;break;
                case 3: color = Color.GREEN;break;
                case 4: color = Color.CYAN;break;
                case 9: color = Color.YELLOW;break;
                default : color = Color.white;
            }
            g.setColor(color);
            g.fillRect(30 * kolom,30 * baris, 30 , 30 );
            g.setColor(color.BLACK);
            g.drawRect(30 * kolom,30 * baris, 30 , 30);
            
        }
    }
}

//Class Main====================================================================
public static void main(String args[]){
    
    SwingUtilities.invokeLater
    
(new Runnable()  
    {   
          @Override
          public void run() 
          {
              
            View view = new View();
            view.setVisible(true);
          }
    }
);

}    
    
}