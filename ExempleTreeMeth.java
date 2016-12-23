import DrawTheTree.*;

/**
 * Juste un simple exemple du fonctionnement avec la création de DrawTree dans une méthode de l'arbre
 * 
 * Documentation complète disponible sur GitHub : https://github.com/Zibeline/DrawTheTree
 * 
 * @author DenisM
 * @version Décembre 2016
 */
public class ExempleTreeMeth {
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
    
    public ExempleTreeMeth() {
        ExempleNode enLeft = new ExempleNode("Gauche", null, null);
        ExempleNode enRight = new ExempleNode("Droite", null, null);
        
        root = new ExempleNode("Racine", enLeft, enRight);
    }
    
    // ***** Partie spécifique à cet exemple *****
    
    public void showTree() {
        DrawTree dt = new DrawTree(this.root);
    }
    
    public static void main(String[] args) {
        ExempleTreeMeth et = new ExempleTreeMeth();
        
        et.showTree();
    }
}