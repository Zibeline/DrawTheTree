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
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import java.awt.BorderLayout;
import javax.swing.BoxLayout;
import java.awt.FlowLayout;

import java.awt.Font;
import java.awt.FontMetrics;

import java.util.Iterator;

import java.awt.Canvas;

import java.util.Stack;
import java.util.HashMap;

import java.awt.Color;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

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
    
    private Stack<DrawNode> nodes; // Pile des noeuds
    
    // Informations sur l'arbre à afficher
    private DrawableNode root; // objet qui représente l'arbre à afficher
    private int treeHeight; // hauteur de l'arbre
    private int treeSize; // taille de l'arbre
    
    private Font font;
    
    // Informations sur les constantes pour l'affichage
    private int lineHeight = 100; // hauteur entre deux lignes
    private int paddingY = 10; // padding en haut et en bas de l'image
    
    private boolean refreshDefault = true; // si on chenge les settings sans préciser si refresh ou pas, que faire par défaut (true donc par défaut on refresh)
    
    // Informations sur les noeuds pour l'affichage
    private int espaceX = 10; // moitié de l'espace qu'il y a entre les deux liens qui partent vers les fils
    
    private boolean openView = false; // True si la fenetre a été ouverte
    private JFrame jFrame; // Frame qui affiche tout
    private Drawer drawer; // Objet qui affiche l'arbre
    
    private DrawParams params;
    private HashMap settingsSecret = new HashMap();
    
    // Taille de l'image (sera calculé dyamiquement, ces valeurs par défaut ne servent à rien)
    private int imgX = 8000;
    private int imgY = 700;
    
    private JTextField taInfos; // Textarea qui sers de console
    private JTextField infoSize; // Textarea qui sers de console
    private JTextField infoHeight; // Textarea qui sers de console
    
    private BufferedImage paintImage; // BufferedImage qui contient la représentation visuelle de l'arbre
    
    /**
     * Enregistre l'image dans le fichier 
     * Ne peut être que en PNG
     * Retourne true si pas d'erreur
     */
    public boolean saveImage(String file) {
        try {
            ImageIO.write(paintImage, "PNG", new File(file));
        } catch (IOException e) {
            setInfo("Erreur enregistrement image");
            return false;
        }
        setInfo("Image enregistrée");
        return true;
    }
    
    /**
     * Affiche une info dans la zone de message (et mets à jour l'affichage des infos sur la taille de l'arbre)
     */
    private void setInfo(String info) {
        if (!openView) return;
        taInfos.setText(info);
        
        infoHeight.setText(Integer.toString(treeHeight));
        infoSize.setText(Integer.toString(treeSize));
    }
    
    public Object getSetting(String key) {
        return params.getParam(key).value;
    }
    
    private void settingsChanged(boolean refresh) {
        font = new Font("Arial", Font.PLAIN, (int) getSetting("fontSize")); // recalcule la font
        
        Canvas c = new Canvas();
        FontMetrics metrics = c.getFontMetrics(font);
        
        settingsSecret.put("nodeHeight", metrics.getAscent()+2*(int)getSetting("nodePadding")); // calcule la node height
        
        if (refresh) {
            if (openView) {   
                for (Object kk : fields.keySet()) {
                    String key = (String) kk.toString();
                    
                    JTextField tmp = fields.get(key);
                    tmp.setText(getSetting(key).toString());                    
                }
            }
            refresh(); // si il faut faire un refresh on le fait
        }
    }
    
    private void setSettings(HashMap newSettings, boolean refresh) {
        params.setAll(newSettings); // place tous les nouveaux settings
        
        settingsChanged(refresh);
    }
    
    /**
     * Affecte une série de settings d'une HashMap sans préciser si on refresh ou pas (par défaut true)
     */
    public void setSettings(HashMap newSettings) {
        setSettings(newSettings, refreshDefault);
    }
    
    /**
     * Pour modifier un setting précis
     */
    private void setSetting(String key, Comparable setting, boolean refresh) {
        HashMap<String, Comparable> newSet = new HashMap(); // crée une map avec le nouveau setting
        newSet.put(key, setting);
        
        setSettings(newSet, refresh);
    }
    
    /**
     * Pour modifier un setting précis sans préciser si on refresh ou pas (par détauf true)
     */
    public void setSetting(String key, Comparable setting) {
        setSetting(key, setting, refreshDefault);
    }
    
    
    private class DrawParams {
        private HashMap<String, DrawSetting> settings;
        
        public DrawParams() {
            settings = new HashMap<String, DrawSetting>();
            
            setParam("fontSize", 15, true);          // largeur d'un noeud
            setParam("nodeWidth", 80, true);         // largeur d'un noeud
            setParam("nodePadding", 3, true);        // padding intérieur de chaque noeud
            setParam("nodeXmargin", 4, true);        // marge à gauche et à droite de chaque noeud
            setParam("transparentBkg", false, false); // arrière plan transparent
            setParam("colorBkg", new Color(255, 255, 0), false); // couleur de fond de l'image
            setParam("colorDefault", Color.black, false);        // couleur par défaut
            setParam("colorAlt", Color.red, false);              // couleur secondaire (red pour red-black)
        }
        
        public void setParam(String key, Object value, boolean editable) {
            settings.put(key, new DrawSetting(value, editable));
        }
        
        public void setParam(String key, Object value) {
            setParam(key, value, settings.get(key).editable);
        }
        
        public void setAll(HashMap newSettings) {
            for (Object key : newSettings.keySet()) {
                
                setParam(key.toString(), newSettings.get(key));
            }
        }
        
        public HashMap getAll() {
            return settings;
        }
        
        public DrawSetting getParam(String key) {
            return settings.get(key);
        }
        
        private class DrawSetting<E> {
            public boolean editable;
            public E value;
        
            public DrawSetting(E value, boolean editable) {
                this.editable = editable;
                this.value = value;
            }
        }
    }
    
    private void setDefaultSettings(boolean refresh) {
        params = new DrawParams();
        settingsChanged(refresh);
    }
    
    private void setDefaultSettings() {
        setDefaultSettings(false);
    }
    
    /**
     * Constructeur
     * Stocke la racine de l'arbre à afficher, crée la frame, et appelle refresh() pour afficher
     */
    public DrawTree(DrawableNode root, boolean youWantTheView, HashMap settings) {
        setDefaultSettings();
        setSettings(settings, false);
        
        
        // Stocke l'arbre a afficher
        this.root = root;
        
        if (youWantTheView)  view();
        
        refresh(); // Appelle refresh pour créer l'image
    }
    
    public DrawTree(DrawableNode root, boolean youWantTheView) {
        this(root, youWantTheView, new HashMap());
    }
    
    public DrawTree(DrawableNode root) {
        this(root, true);
    }
    
    private void view() {
        if (openView) return;
        
        jFrame = new DrawFrame(this); // Crée la frame qui va tout afficher
        
        openView = true;
    }
    
    public void refresh(DrawableNode root) {
        this.root = root;
        
        refresh();
    }
    
    /**
     * Raffraichit l'affichage de l'arbre
     * Est utile pour soit l'afficher une premi�re fois soit pour le mettre � jour apr�s qu'il ait �t� modifi�
     */
    public void refresh() {
        this.treeHeight = 0; // Réinitialise la hauteur de l'arbre
        this.treeSize = 0; // Réinitialise la hauteur de l'arbre
        nodes = new Stack<DrawNode>(); // Remets à zéro la pile de noeuds

        addNode(this.root, 0, 1, null); // Ajout du premier node (les autres s'ajouteront récursivement)
        
        this.treeHeight++; // Incrémente pour prendre le premier noeud en compte
        
        computeImageSize(); // Calcule la taille de l'image à créer
        
        createImage(); // Crée l'image
       
        while(!nodes.empty()) {
            imageDrawNode(nodes.pop());
        }
        
        if (openView) {
            drawer.setPreferredSize(new Dimension(imgX, imgY)); // Taille de l'image en fonction des valeurs qu'on vient de calculer
            
            drawer.repaint();
        }
        setInfo("Image raffraichie");
    }
    
    /**
     * Calcule la taille de l'image
     */
    private void computeImageSize() {
        // Pour la largeur 2^treeHeight nous donne le nombre max de noeuds en parall�le
        // Ensuite on limte cette taille à la taille max
        this.imgX = (int) Math.pow(2, this.treeHeight) * ((int)getSetting("nodeWidth")+2*(int)getSetting("nodeXmargin"));
        if (this.imgX > maxX)this.imgX = maxX;
        
        // Y = (hauteur d'une ligne + hauteur d'un noeud) * nombre de lignes + (2*padding)
        this.imgY = ((this.treeHeight)*(lineHeight+(int)settingsSecret.get("nodeHeight")))-lineHeight+(2*paddingY);
        if (this.imgY > maxY) {
            this.imgY = maxY;
        
            int dispY = this.imgY-(2*paddingY);
            this.lineHeight = (int) Math.ceil(dispY/(this.treeHeight))-(int)settingsSecret.get("nodeHeight");
        }
    }
    
    private void createImage() {
        paintImage = new BufferedImage(imgX, imgY, BufferedImage.TYPE_4BYTE_ABGR); // Crée une nouvelle image
        Graphics g = paintImage.createGraphics();
        
        if (!(boolean)getSetting("transparentBkg")) { // Mets la couleur de fond de l'image (uniquement si transparentBkg est false)
            g.setColor((Color) getSetting("colorBkg"));
            g.fillRect(0, 0, paintImage.getWidth(), paintImage.getHeight());
        }
        
        g.dispose();
    }
    
    /**
     * Dessine un noeud
     */
    private void imageDrawNode(DrawNode noeud) {
        Graphics g = paintImage.createGraphics();

        g.setColor((Color) getSetting("colorDefault")); // mets la couleur par défaut
        g.setFont(font);
        
        // Calcule la position x et y
        noeud.calculatePos();
        
        FontMetrics metrics = g.getFontMetrics();
        
        g.drawRect(noeud.x-Math.round((int)getSetting("nodeWidth")/2), noeud.y, (int) getSetting("nodeWidth"), (int) settingsSecret.get("nodeHeight")); // trace le rectangle du noeud
        
        int usableWidth = (int)getSetting("nodeWidth")-(int)getSetting("nodePadding");
        
        String label = noeud.label;
        
        if (metrics.stringWidth(label)>usableWidth) {
            while (label.length()>0 && metrics.stringWidth(label+"~")>usableWidth) {
                label = label.substring(0, label.length()-1);
            }
        }
        if (label.length()<noeud.label.length()) label += "~";
        
        g.drawString(label, noeud.x-Math.round(metrics.stringWidth(label)/2), noeud.y+(int)settingsSecret.get("nodeHeight")-(int)getSetting("nodePadding"));
        
        // Si il y a un noeud parent, on va afficher le lien vers celui ci
        if (noeud.parent != null) {
            noeud.parent.calculatePos();
            if (noeud.color) g.setColor((Color) getSetting("colorAlt")); // si ce noeud est red, le lien depuis son parent sera dessiné en couleur alt (rouge par défaut)
            // calcule pour décaler le x de fin pour ne pas que les deux liens se touchent
            // si le parent est plus à droite on diminue le x et sinon on l'augmente
            int endX = (noeud.parent.x-noeud.x>0) ? noeud.parent.x-espaceX : noeud.parent.x+espaceX;
            g.drawLine(noeud.x, noeud.y, endX, noeud.parent.y+(int)settingsSecret.get("nodeHeight"));
            g.setColor((Color) getSetting("colorDefault"));
        }
        
        g.dispose();
    }
    
    /**
     * Ajoute une node
     * Va transformer une node telle que définie dans le tree en node telle que nous on les aime (à savoir avec leur position, ...)
     * line représente la ligne. 
     * Pour index voir réponse ici : http://stackoverflow.com/questions/14184655/set-position-for-drawing-binary-tree
     */
    private void addNode(DrawableNode runner, int line, int index, DrawNode parent) {
        this.treeHeight = Math.max(line, this.treeHeight);
        this.treeSize++; // incrémente la taille de l'arbre
        
        DrawNode node = new DrawNode(line, index, runner.DrawableLabel(), runner.DrawableRed(), parent); // Crée le nouvel objet
        nodes.push(node); // l'ajoute dans la stack
        
        // Si enfant, on l'ajoute
        if (runner.DrawableLeft() != null) addNode(runner.DrawableLeft(), line+1, 2*index-1, node);
        if (runner.DrawableRight() != null) addNode(runner.DrawableRight(), line+1, 2*index, node);
    }
    
    /**
     * Représente un node de manière compréhensible et utilisable par note programme pour pouvoir le dessiner
     */
    private class DrawNode {
        public int x, y, line, index;
        public String label;
        public boolean color;
        public DrawNode parent;
        
        public DrawNode(int line, int index, String label, boolean color) {
            this(line, index, label, color, null);
        }
        
        public DrawNode(int line, int index, String label, boolean color, DrawNode parent) {
            this.x      = 0;
            this.y      = 0;
            this.line   = line;
            this.index  = index;
            this.label  = label;
            this.color  = color;
            this.parent = parent;
        }
        
        /**
         * Calcule la position du noeud sur base de sa ligne et son index après avoir calculé la taille de l'image
         */
        public void calculatePos() {
            this.x = this.index * imgX / (1 + (int) Math.pow(2, this.line));
            this.y = this.line*(lineHeight+(int)settingsSecret.get("nodeHeight"))+paddingY;
        }
    }
    
    private HashMap<String, JTextField> fields;
    
    /**
     * Panel pour les settings
     */
    private class DrawSettings extends JPanel implements ActionListener {
        private DrawTree parent;
        
        public DrawSettings(DrawTree parent) {
            this.parent = parent;
        
            this.setLayout(new GridBagLayout());
         
            GridBagConstraints constraints = new GridBagConstraints();
            constraints.anchor = GridBagConstraints.WEST;
            constraints.insets = new Insets(10, 10, 10, 10);
            
            fields = new HashMap<String, JTextField>();
            
            int i = 0;
            for (Object kk : parent.params.getAll().keySet()) {
                String key = (String) kk.toString();
                
                if (parent.params.getParam(key).editable) {
                    constraints.gridy = i;
                    constraints.gridx = 0;     
                    this.add(new JLabel(key+" : "), constraints);
                         
                    JTextField field = new JTextField(6);
                    field.setText(parent.getSetting(key).toString());
                    field.addActionListener(this);
                    
                    constraints.gridx = 1;
                    this.add(field, constraints);
                    
                    fields.put(key, field);
                    
                    i++;
                }
            }
        }
    
        public void actionPerformed(ActionEvent arg0) {
            for (String key : fields.keySet()) {
                parent.setSetting(key, Integer.parseInt(fields.get(key).getText()), false);
            }
            parent.refresh();
        }
    }
    
    /**
     * Fenetre globale
     */
    private class DrawFrame extends JFrame implements ActionListener {
        private JPanel container = new JPanel();
        
        private JPanel sidebar = new JPanel();
        private HashMap<String, JButton> buttons;
        
        private DrawTree parent;
        
        public DrawFrame(DrawTree parent) {
            this.parent = parent;
            
            this.setTitle("DrawTheTree - A little tool by DenisM");
            this.setLayout(new BorderLayout());
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            this.setLocationRelativeTo(null);
                
            drawer = new Drawer(); // ??
            drawer.setBorder(BorderFactory.createLineBorder(Color.orange));
                
            JScrollPane scrollPane = new JScrollPane(drawer);
            scrollPane.setPreferredSize(new Dimension(300, 300)); // ??
            scrollPane.setBorder(BorderFactory.createLineBorder(Color.orange));
            
            container.setBackground(Color.LIGHT_GRAY);
            container.setLayout(new BorderLayout());
                
            sidebar.setBackground(Color.orange);
            sidebar.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
            sidebar.setPreferredSize(new Dimension(300, 600));
                
            taInfos = new JTextField(20);
            taInfos.setEditable(false);
            
            buttons = new HashMap<String, JButton>();
            buttons.put("save", new JButton("Exporter une image png"));
            buttons.put("reload", new JButton("Rafraichir l'image"));
            buttons.put("bkg", new JButton("Basculer arrière-plan"));
            buttons.put("reset", new JButton("Paramètres par défaut"));
            
            JLabel act = new JLabel("Actions :");
            act.setFont(new Font("Century Schoolbook L", 2, 24));
            sidebar.add(act);
            
            sidebar.add(buttons.get("save"));
            sidebar.add(buttons.get("reset"));
            sidebar.add(buttons.get("bkg"));
            sidebar.add(buttons.get("reload"));
            
            JLabel modif = new JLabel("Modifier les paramètres :");
            modif.setFont(new Font("Century Schoolbook L", 2, 24));
            sidebar.add(modif);

            JPanel settings = new DrawSettings(parent);
            settings.setMaximumSize(new Dimension(200,300));
            sidebar.add(settings);
            
            JLabel infos = new JLabel("Messages :");
            infos.setFont(new java.awt.Font("Century Schoolbook L", 2, 24));
            sidebar.add(infos);
            sidebar.add(taInfos);

            
            JLabel inf = new JLabel("Informations :");
            inf.setFont(new Font("Century Schoolbook L", 2, 24));
            sidebar.add(inf);
            
            JPanel informations = new JPanel();
            informations.setLayout(new GridBagLayout());
         
            GridBagConstraints constraints = new GridBagConstraints();
            constraints.anchor = GridBagConstraints.WEST;
            constraints.insets = new Insets(10, 10, 10, 10);
            
            infoSize = new JTextField(6);
            infoSize.setEditable(false);
            
            infoHeight = new JTextField(6);
            infoHeight.setEditable(false);
            
            constraints.gridy = 1;
            constraints.gridx = 0;     
            informations.add(new JLabel("Taille : "), constraints);
            constraints.gridx = 1;     
            informations.add(infoSize, constraints);
            
            constraints.gridy = 2;
            constraints.gridx = 0;     
            informations.add(new JLabel("Hauteur : "), constraints);
            constraints.gridx = 1;     
            informations.add(infoHeight, constraints);
                         
            sidebar.add(informations);
            
            try {
                BufferedImage myPicture = ImageIO.read(new File("DrawTheTree/tree.png"));
                JLabel picLabel = new JLabel(new ImageIcon(myPicture));
                sidebar.add(picLabel);
            } catch (IOException e) {}
            
            for (String key : buttons.keySet()) {
                buttons.get(key).addActionListener(this);
            }
            
            container.add(sidebar, BorderLayout.WEST);
            container.add(scrollPane, BorderLayout.CENTER);
            
            this.setSize(800, 600);

            this.setContentPane(container);
            this.setVisible(true);
        }
        
        public void actionPerformed(ActionEvent arg0) {
            for (String key : buttons.keySet()) {
                if (buttons.get(key)==arg0.getSource()) {
                    if (key=="save") {
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
                            setInfo("Enregistrement annulé");
                        }
                    }
                    else if (key=="reload") {
                        parent.refresh();
                    }
                    else if (key=="bkg") {
                        parent.setSetting("transparentBkg", !(boolean)parent.getSetting("transparentBkg"));
                        parent.refresh();
                    }
                    else if (key=="reset") {
                        parent.setDefaultSettings(true);
                        parent.refresh();
                    }
                }       
            }
        }
    }
    
    /**
     * Panel qui affiche l'image générée. Pour le mettre à jour il suffit d'appeler Drawer.repaint()
     */
    private class Drawer extends JPanel {
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(paintImage, 0, 0, null);
        }
    }
}