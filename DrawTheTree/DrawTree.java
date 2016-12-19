package DrawTheTree;

import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JFileChooser;
import javax.swing.JTextArea;

import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;

import javax.swing.Box;

import javax.swing.JScrollPane;
import java.awt.Dimension;

import javax.swing.BorderFactory;

import java.awt.BorderLayout;
import javax.swing.BoxLayout;


import java.awt.Color;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

import java.util.*;

/**
 * Write a description of class DrawTool here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class DrawTree {
    private JFrame jFrame;
    private Drawer drawer;
    
    private DrawableTree tree;
    
    private int x = 8000;
    private int y = 700;
    
    private int paddingY = 10;
    
    private int lineHeight;
    
    private int nodeHeight = 15;
    private int nodeWidth = 80;
    
    private JTextArea taInfos;
    
    public class Drawer extends JPanel {
        private BufferedImage paintImage;// = new BufferedImage(x, y, BufferedImage.TYPE_4BYTE_ABGR);
        
        public Drawer () {
            clean();
        }
        
        public void clean() {
            paintImage = new BufferedImage(x, y, BufferedImage.TYPE_4BYTE_ABGR);
            Graphics g = paintImage.createGraphics();
            
            g.setColor ( new Color ( 255, 255, 255 ) );
            g.fillRect ( 0, 0, paintImage.getWidth(), paintImage.getHeight() );
            
            g.dispose();
            repaint();
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(paintImage, 0, 0, null);
        }
            
        public void save(String file) throws IOException{
            ImageIO.write(paintImage, "PNG", new File(file));
        }
        
        public void addNode(DrawNode noeud) {
            Graphics g = paintImage.createGraphics();

            g.setColor(Color.black);
            
            g.drawRect(noeud.x-Math.round(nodeWidth/2), noeud.y, nodeWidth, nodeHeight);
            int labelLimit = 10;
            String label = (noeud.label.length()>labelLimit) ? noeud.label.substring(0,labelLimit)+"~" : noeud.label;
            g.drawString(label, noeud.x-Math.round(nodeWidth/2)+2, noeud.y+nodeHeight-2);
                
            if (noeud.parent != null) {
                if (noeud.color) g.setColor(Color.red);
                g.drawLine(noeud.x, noeud.y, noeud.parent.x, noeud.parent.y+nodeHeight);
                g.setColor(Color.black);
            }
            
            g.dispose();
            repaint();
        }
    }
    
    public class DrawNode {
        public int x;
        public int y;
        
        public DrawNode parent;
        
        public String label;
        public boolean color;
        
        public DrawNode(int x, int y, String label, boolean color) {
            this.x      = x;
            this.y      = y;
            this.label  = label;
            this.color  = color;
            this.parent = null;
        }
        
        public DrawNode(int x, int y, String label, boolean color, DrawNode parent) {
            this.x      = x;
            this.y      = y;
            this.label  = label;
            this.color  = color;
            this.parent = parent;
        }
    }
    
    public class DrawFrame extends JFrame implements ActionListener {
        private JPanel container = new JPanel();
        
        private JPanel sidebar = new JPanel();
        
        private JButton btnSave = new JButton("Exporter en png");
        private JButton btnRefresh = new JButton("Rafraichir");
        
        private JLabel lbInfos;
        
        private DrawTree parent;
        
        public DrawFrame(DrawTree parent) {
            this.parent = parent;
            this.setTitle("DrawTheTree - DenisM");
            this.setLayout(new BorderLayout());
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            this.setLocationRelativeTo(null);
            
            drawer = new Drawer(); // ??
        
            JScrollPane scrollPane = new JScrollPane(drawer);
            scrollPane.setPreferredSize(new Dimension(300, 300)); // ??
            scrollPane.setBorder(BorderFactory.createLineBorder(Color.orange));
        
            container.setBackground(Color.LIGHT_GRAY);
            container.setLayout(new BorderLayout());
            
            sidebar.setBackground(Color.orange);
            sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
            sidebar.setPreferredSize(new Dimension(300, 300));
            
            lbInfos = new JLabel("Infos sur le graphe");
            
            taInfos = new JTextArea(5, 20);
            JScrollPane tapInfos = new JScrollPane(taInfos); 
            
            taInfos.setPreferredSize(new Dimension(25, 25));
            taInfos.setEditable(false);
            
            sidebar.add(Box.createVerticalGlue());
            sidebar.add(btnSave);
            sidebar.add(Box.createRigidArea(new Dimension(0,20)));
            sidebar.add(lbInfos);
            sidebar.add(tapInfos);
            sidebar.add(Box.createRigidArea(new Dimension(0,20)));
            sidebar.add(btnRefresh);
            
            sidebar.add(Box.createRigidArea(new Dimension(0,50)));
        
            sidebar.add(Box.createVerticalGlue());
            
            try {
                BufferedImage myPicture = ImageIO.read(new File("DrawTheTree/tree.png"));
                JLabel picLabel = new JLabel(new ImageIcon(myPicture));
                sidebar.add(picLabel);
            } catch (IOException e) {}
            
            
            
            btnSave.addActionListener(this);
            btnRefresh.addActionListener(this);
            
            container.add(sidebar, BorderLayout.WEST);
            container.add(scrollPane, BorderLayout.CENTER);
            
            
            
            this.setSize(800, 600);

            this.setContentPane(container);
            this.setVisible(true);
        }
        
        public void actionPerformed(ActionEvent arg0) {
            System.out.println(arg0.getSource());
            if(arg0.getSource() == btnSave) {
                
                JFileChooser c = new JFileChooser();
                
                c.setSelectedFile(new File("DrawTheTree_export.png"));
                c.setFileFilter(new FileNameExtensionFilter("png file","png"));
                
                int rVal = c.showSaveDialog(DrawFrame.this);
                
                if (rVal == JFileChooser.APPROVE_OPTION) {
                    String file = c.getSelectedFile().getName();
                    String path = c.getCurrentDirectory().toString();
                    String ff = path+"\\"+file;
                    try {
                        drawer.save(ff);
                    } catch (IOException e) {}
                }
                if (rVal == JFileChooser.CANCEL_OPTION) {
                    //filename.setText("You pressed cancel");
                    //dir.setText("");
                }
            }
            
            if(arg0.getSource() == btnRefresh) {
                parent.refresh();
            }
        }
    }
    
    public void addInfo(String info) {
        taInfos.append(info+"\n");
    }
    
    public DrawTree(DrawableTree tree) {
        this.tree = tree;
        jFrame = new DrawFrame(this);
        
        refresh();
    }
    
    public void refresh() {
        int treeHeight = tree.DrawableHeight();
        
        this.x = (int) Math.pow(2, treeHeight) * (nodeWidth+8);
        if (this.x > 15000)this.x = 15000;
        
        this.y = (treeHeight+1)*120;
        if (this.y > 900)this.y = 900;
        
        int dispoY = this.y-2*paddingY-nodeHeight;
        this.lineHeight = (int) Math.floor(dispoY/(treeHeight+1));
        
        drawer.clean();
        
            drawer.setPreferredSize(new Dimension(x, y)); 
        
        addInfo("~~~~ REFRESH ~~~~");
        
        
        //addInfo("Size : "+tree.size());
        addInfo("Height : "+treeHeight);
        /*addInfo("isEmpty : "+tree.isEmpty());
        addInfo("isEmpty : "+tree.isEmpty());
        */
        addNode(tree.DrawableRoot(), 0, 1, null);
    }
    
    public void addNode(DrawableNode runner, int line, int index, DrawNode parent) {
        int posX = index * x / (1 + (int) Math.pow(2, line));
        int posY = line*lineHeight+paddingY;
        
        DrawNode node = new DrawNode(posX, posY, runner.DrawableLabel(), runner.DrawableRed(), parent);
        drawer.addNode(node);
        
        if (runner.DrawableLeft() != null) addNode(runner.DrawableLeft(), line+1, 2*index-1, node);
        if (runner.DrawableRight() != null) addNode(runner.DrawableRight(), line+1, 2*index, node);
    }
}
