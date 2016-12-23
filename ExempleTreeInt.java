import DrawTheTree.*;

/**
 * Juste un simple exemple du fonctionnement avec la création de DrawTree dans le constructeur de l'arbre
 * 
 * Documentation complète disponible sur GitHub : https://github.com/Zibeline/DrawTheTree
 * 
 * @author DenisM
 * @version Décembre 2016
 */
public class ExempleTreeInt {
    private ExempleNode root;

    public class ExempleNode implements DrawableNode {
        public ExempleNode left;
        private ExempleNode right;
        private String label;
        
        public ExempleNode (String label, ExempleNode left, ExempleNode right) {
            this.left = left;
            this.right = right;
            this.label = label;
        }
        
        public ExempleNode DrawableLeft() {
            return left;
        }
        
        public ExempleNode DrawableRight() {
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
        ExempleNode enLeft = new ExempleNode("ABC", null, null);
        ExempleNode enRight = new ExempleNode("DEF", null, null);
        
        root = new ExempleNode("GHI", enLeft, enRight);
        
        dt = new DrawTree(this.root);
    }
        
    public static void main(String[] args) {
        ExempleTreeInt et = new ExempleTreeInt();
    }
}