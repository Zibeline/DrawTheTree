package DrawTheTree;

import java.awt.Graphics;

// Pour les frames, fenetres et tout
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

// Pour le dialogue pour enregistrer
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

// Pour écouter les events dans les fenetres
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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

/**
 * Classe pour permettre de visualiser une implémantation d'arbre
 * 
 * Documentation complète disponible sur GitHub : https://github.com/Zibeline/DrawTheTree
 * 
 * @author DenisM
 * @version Décembre 2016
 */
public class DrawTree {
    // Taille maximale des images produites (si on fait plus grand on as des erreurs)
    private int maxX = 15000;
    private int maxY = 900;
    
    // Informations sur l'arbre � afficher
    private DrawableTree tree; // objet qui repr�sente l'arbre � afficher
    private int treeHeight; // hauteur de l'arbre
    
    // Informations sur les constantes pour l'affichage
    private int nodeXpadding = 4; // marge � gauche et � droite de chaque noeud
    private int lineHeight = 100; // hauteur entre deux lignes
    private int paddingY = 10; // padding en haut et en bas de l'image
    
    // Informations sur les noeuds pour l'affichage
    private int nodeHeight = 15; // leur hauteur
    private int nodeWidth = 80; // leur largeut
    private int labelLimit = 10; // nombre max de char dans le string du label (le reste sera enlevé et remplacé par ~)
    private int espaceX = 10; // moitié de l'espace qu'il y a entre les deux liens qui partent vers les fils
    
    // Frame qui affiche tout
    private JFrame jFrame;
    
    // Objet qui affiche l'arbre
    private Drawer drawer;
    //private JPanel drawer;
    
    // Taille de l'image (sera calculé dyamiquement, ces valeurs par défaut ne servent à rien)
    private int x = 8000;
    private int y = 700;
    
    // Couleurs pour un peu tout
    private Color colorBkg = new Color(255, 255, 255); // couleur de fond de l'image
    private Color colorDefault = Color.black; // couleur par défaut
    private Color colorAlt = Color.red; // couleur secondaire (red pour red-black)
    
    // Textarea qui sers de console
    private JTextArea taInfos;
    
    // BufferedImage qui contient la représentation visuelle de l'arbre
    private BufferedImage paintImage;
    
    private void imageClean() {
        // Crée une nouvelle image
        paintImage = new BufferedImage(x, y, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics g = paintImage.createGraphics();
        
        // Mets la couleur de fond de l'image
        g.setColor(colorBkg);
        g.fillRect(0, 0, paintImage.getWidth(), paintImage.getHeight());
        
        g.dispose();
    }
    
    // Dessine un nouveau node
    public void imageDrawNode(DrawNode noeud) {
        Graphics g = paintImage.createGraphics();

        g.setColor(colorDefault); // mets la couleur par défaut
        
        g.drawRect(noeud.x-Math.round(nodeWidth/2), noeud.y, nodeWidth, nodeHeight); // trace le rectangle du noeud
        
        // Découpe le label si nécessaire (en fonction de labelLimit) et l'affiche
        String label = (noeud.label.length()>labelLimit) ? noeud.label.substring(0,labelLimit)+"~" : noeud.label;
        g.drawString(label, noeud.x-Math.round(nodeWidth/2)+2, noeud.y+nodeHeight-2);
        
        // Si il y a un noeud parent, on va afficher le lien vers celui ci
        if (noeud.parent != null) {
            if (noeud.color) g.setColor(colorAlt); // si ce noeud est red, le lien depuis son parent sera dessiné en couleur alt (rouge par défaut)
            // calcule pour décaler le x de fin pour ne pas que les deux liens se touchent
            // si le parent est plus à droite on diminue le x et sinon on l'augmente
            int endX = (noeud.parent.x-noeud.x>0) ? noeud.parent.x-espaceX : noeud.parent.x+espaceX;
            g.drawLine(noeud.x, noeud.y, endX, noeud.parent.y+nodeHeight);
            g.setColor(colorDefault);
        }
        
        g.dispose();
    }
    
    private class Drawer extends JPanel {
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(paintImage, 0, 0, null);
        }
    }
    
    /**
     * Représente un node de manière compréhensible et utilisable pour pouvoir le dessiner
     */
    private class DrawNode {
        public int x;
        public int y;
        public String label;
        public boolean color;
        public DrawNode parent;
        
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
    
    /**
     * Panel pour les settings
     */
    private class DrawSettings extends JPanel implements ActionListener {
        private JTextField inpNodeWidth;
        private JTextField inpNodeHeight;
    
        private DrawTree parent;
        
        public DrawSettings(DrawTree parent) {
            this.parent = parent;
        
            this.setLayout(new GridBagLayout());
         
            GridBagConstraints constraints = new GridBagConstraints();
            constraints.anchor = GridBagConstraints.WEST;
            constraints.insets = new Insets(10, 10, 10, 10);
         
            inpNodeWidth = new JTextField(6);
            inpNodeWidth.setText(Integer.toString(parent.setNodeWidth(-1)));
            inpNodeWidth.addActionListener(this);
         
            inpNodeHeight = new JTextField(6);
            inpNodeHeight.setText(Integer.toString(parent.setNodeHeight(-1)));
            inpNodeHeight.addActionListener(this);
            
            // add components to the panel
            constraints.gridx = 0;
            constraints.gridy = 0;     
            this.add(new JLabel("Largeur noeud :"), constraints);
 
            constraints.gridx = 1;
            this.add(inpNodeWidth, constraints);
            
            // add components to the panel
            constraints.gridx = 0;
            constraints.gridy = 1;     
            this.add(new JLabel("Hauteur noeud :"), constraints);
 
            constraints.gridx = 1;
            this.add(inpNodeHeight, constraints);
         
        }
    
        public void actionPerformed(ActionEvent arg0) {
            // En fonction de l'action mets à jour les settings
            if(arg0.getSource() == inpNodeWidth)  parent.setNodeWidth(Integer.parseInt(inpNodeWidth.getText()));
            if(arg0.getSource() == inpNodeHeight)  parent.setNodeHeight(Integer.parseInt(inpNodeHeight.getText()));
        }
    }
    
    /**
     * Fenetre globale
     */
    private class DrawFrame extends JFrame implements ActionListener {
        private JPanel container = new JPanel();
        
        private JPanel sidebar = new JPanel();
        
        private JButton btnSave = new JButton("Exporter une image png");
        private JButton btnRefresh = new JButton("Rafraichir");
        
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
            //sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
            sidebar.setPreferredSize(new Dimension(300, 300));
            
            taInfos = new JTextArea(5, 20);
            JScrollPane tapInfos = new JScrollPane(taInfos); 
            taInfos.setPreferredSize(new Dimension(25, 25));
            taInfos.setEditable(false);
            
            JPanel settings = new DrawSettings(parent);
            
            sidebar.add(Box.createVerticalGlue());
            sidebar.add(btnSave);
            sidebar.add(new JLabel("Modifier les paramètres"));
            sidebar.add(settings);
            sidebar.add(Box.createRigidArea(new Dimension(0,20)));
            
            sidebar.add(new JLabel("Infos sur l'arbre"));
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
            // Si on a appuyé pour enregistrer l'image, dialogue pour choisir ou enregistrer puis appel de méthode pour enregistrer
            if(arg0.getSource() == btnSave) {
                JFileChooser c = new JFileChooser();
                
                c.setSelectedFile(new File("DrawTheTree_export.png"));
                c.setFileFilter(new FileNameExtensionFilter("png file","png"));
                
                int rVal = c.showSaveDialog(DrawFrame.this);
                
                if (rVal == JFileChooser.APPROVE_OPTION) {
                    String file = c.getSelectedFile().getName();
                    String path = c.getCurrentDirectory().toString();
                    String ff = path+"\\"+file;
                    
                    parent.saveImage(ff);
                }
                if (rVal == JFileChooser.CANCEL_OPTION) {
                    //filename.setText("You pressed cancel");
                    //dir.setText("");
                }
            }
            
            // Si appuryé sur refresh, appel de refresh (obvious)
            if(arg0.getSource() == btnRefresh) {
                parent.refresh();
            }
        }
    }
    
    /**
     * Enregistre l'image dans le fichier 
     * Ne peut être que en PNG
     * Retourne true si pas d'erreur
     */
    public boolean saveImage(String file) {
        try {
            ImageIO.write(paintImage, "PNG", new File(file));
        } catch (IOException e) {
            addInfo("L'image a pas pu être enregistrée");
            return false;
        }
        addInfo("L'image a été enregistrée");
        return true;
    }
    
    /**
     * Ajoute une ligne d'infos dans la console de gauche
     */
    public void addInfo(String info) {
        if (openView) taInfos.append(info+"\n");
    }
    
    /**
     * Constructeur
     * 
     * Stocke l'arbre à afficher, crée la frame, et appelle refresh() pour afficher
     */
    public DrawTree(DrawableTree tree, boolean youWantTheView) {
        // Stocke l'arbre a afficher
        this.tree = tree;
        
        if (youWantTheView)  view();
        
        // Appelle refresh pour afficher
        refresh();
    }
    
    public DrawTree(DrawableTree tree) {
        this(tree, true);
    }
    
    private boolean openView = false;
    
    public void view() {
        if (openView) return;
        
        // Crée la frame qui va tout afficher
        jFrame = new DrawFrame(this);
        
        openView = true;
    }
    
    /**
     * Raffraichit l'affichage de l'arbre
     * Est utile pour soit l'afficher une premi�re fois soit pour le mettre � jour apr�s qu'il ait �t� modifi�
     */
    public void refresh() {
        // Récupère la hauteur de l'arbre (en faisant appel � la m�thode ajout�e dans la classe de l'arbre)
        treeHeight = tree.DrawableHeight();
        
        // Calcule la taille de l'image � cr�er
        
        // Pour la largeur 2^treeHeight nous donne le nombre max de noeuds en parall�le
        // Ensuite on limte cette taille � la taille max
        this.x = (int) Math.pow(2, treeHeight) * (nodeWidth+2*nodeXpadding);
        if (this.x > maxX)this.x = maxX;
        
        // Pour y, hauteur d'une ligne * nombre de lignes + on ajoute le padding
        // Ensuite on limte cette taille � la taille max
        this.y = ((treeHeight+1)*(lineHeight+nodeHeight))+(2*paddingY);
        if (this.y > maxY)this.y = maxY;
        
        // On recalcule qd même la lineHeight pour si jamais y a été limité en size
        int dispY = this.y-(2*paddingY);
        this.lineHeight = (int) Math.ceil(dispY/(treeHeight+1));
        
        // Nettoie l'image
        imageClean();
        
        // Ajout du premier node (les autres s'ajouteront récursivement)
        addNode(tree.DrawableRoot(), 0, 1, null);
        
        if (openView) {
            // Taille de l'image en fonction des valeurs qu'on vient de calculer
            drawer.setPreferredSize(new Dimension(x, y)); 
            
            // Affichage des infos
            addInfo("~~~~ REFRESH ~~~~");
            addInfo("Size : "+tree.DrawableSize());
            addInfo("Height : "+treeHeight);
            
            drawer.repaint();
        }
    }
    
    /**
     * Ajoute une node
     * Va transformer une node telle que définie dans le tree en node telle que nous on les aime (à savoir avec leur position, ...)
     * line représente la ligne. 
     * Pour index voir réponse ici : http://stackoverflow.com/questions/14184655/set-position-for-drawing-binary-tree
     */
    private void addNode(DrawableNode runner, int line, int index, DrawNode parent) {
        // Calcule la position x et y
        int posX = index * x / (1 + (int) Math.pow(2, line));
        int posY = line*lineHeight+paddingY;
        
        // Crée le nouvel objet et l'ajoute à l'image
        DrawNode node = new DrawNode(posX, posY, runner.DrawableLabel(), runner.DrawableRed(), parent);
        imageDrawNode(node);
        
        // Si enfant, on l'ajoute
        if (runner.DrawableLeft() != null) addNode(runner.DrawableLeft(), line+1, 2*index-1, node);
        if (runner.DrawableRight() != null) addNode(runner.DrawableRight(), line+1, 2*index, node);
    }
    
    /**
     * Définis la largeur du rectangle d'un node
     * Si le paramètre est -1, ne change rien (renvoie juste la valeur actuelle)
     */
    public int setNodeWidth(int width) {
        if (width>-1) this.nodeWidth = width;
        refresh();
        return this.nodeWidth;
    }
    
    /**
     * Définis la hauteur du rectangle d'un node
     * Si le paramètre est -1, ne change rien (renvoie juste la valeur actuelle)
     */
    
    public int setNodeHeight(int height) {
        if (height>-1) this.nodeHeight = height;
        refresh();
        return this.nodeHeight;
    }
}