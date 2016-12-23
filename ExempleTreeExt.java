import DrawTheTree.*;

import java.util.HashMap;
import java.awt.Color;

/**
 * Juste un simple exemple du fonctionnement avec la création de DrawTree en dehors de l'arbre
 * 
 * Documentation complète disponible sur GitHub : https://github.com/Zibeline/DrawTheTree
 * 
 * @author DenisM
 * @version Décembre 2016
 */
public class ExempleTreeExt {
    public ExempleNode root;
    
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
    
    public ExempleTreeExt() {
        ExempleNode enLeft = new ExempleNode("Gauche Lorem ipsum dolor", null, null);
        ExempleNode enRight = new ExempleNode("Droite sit amet blabla", null, null);
        
        root = new ExempleNode("Racine", enLeft, enRight);
    }
    
    public static void main(String[] args) {
        ExempleTreeExt et = new ExempleTreeExt();
        
        HashMap setts = new HashMap();
        setts.put("colorBkg", new Color(255, 0, 255));
        
        DrawTree dt = new DrawTree(et.root, true, setts);
        //dt.setSetting("fontSize", 22);
        
    }
}