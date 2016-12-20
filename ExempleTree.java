import DrawTheTree.*;

/**
 * Juste un simple exemple du fonctionnement
 * 
 * Documentation complète disponible sur GitHub : https://github.com/Zibeline/DrawTheTree
 * 
 * @author DenisM
 * @version Décembre 2016
 */
public class ExempleTree implements DrawableTree{
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
    
    public ExempleTree() {
        ExempleNode enLeft = new ExempleNode("Gauche", null, null);
        ExempleNode enRight = new ExempleNode("Droite", null, null);
        
        root = new ExempleNode("Racine", enLeft, enRight);
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
        ExempleTree et = new ExempleTree();
        
        DrawTree dt = new DrawTree(et, true);
    }
}
