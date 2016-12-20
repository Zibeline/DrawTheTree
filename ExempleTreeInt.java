import DrawTheTree.*;

/**
 * Juste un simple exemple du fonctionnement avec la création de DrawTree dans le constructeur de l'arbre
 * 
 * Documentation complète disponible sur GitHub : https://github.com/Zibeline/DrawTheTree
 * 
 * @author DenisM
 * @version Décembre 2016
 */
public class ExempleTreeInt implements DrawableTree{
    private ExempleNode root;

    public class ExempleNode implements DrawableNode {
        private ExempleNode left;
        private ExempleNode right;
        private String label;
        
        public ExempleNode (String label, ExempleNode left, ExempleNode right) {
            this.left = left;
            this.right = right;
            this.label = label;
        }
        
        public DrawableNode DrawableLeft() {
            return left;
        }
        
        public DrawableNode DrawableRight() {
            return right;
        }

        public String DrawableLabel() {
            return label;
        }
        
        public boolean DrawableRed() {
            return false;
        }
    }
    
    // ***** Partie spécifique à cet exemple *****
    private DrawTree dt;
    
    public ExempleTreeInt() {
        ExempleNode enLeft = new ExempleNode("Gauche", null, null);
        ExempleNode enRight = new ExempleNode("Droite", null, null);
        
        root = new ExempleNode("Racine", enLeft, enRight);
        
        dt = new DrawTree(this);
    }
    
    public int DrawableSize() {
        return 3;
    }
    
    public int DrawableHeight() {
        return 2;
    }
    
    public DrawableNode DrawableRoot() {
        return root;
    }
    
    public static void main(String[] args) {
        ExempleTreeInt et = new ExempleTreeInt();
    }
}