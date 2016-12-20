package DrawTheTree;

/**
 * Interface des méthdes que l'implémentation d'un noeud (node) de l'arbre doit avoir
 * 
 * Documentation complète disponible sur GitHub : https://github.com/Zibeline/DrawTheTree
 * 
 * @author DenisM
 * @version Décembre 2016
 */
public interface DrawableNode {
    /**
     * Renvoie la sous node de gauche 
     */
    public DrawableNode DrawableLeft();
    
    /**
     * Renvoie la sous node de droite
     */
    public DrawableNode DrawableRight();

    /**
     * Renvoie un string qui sera affiché pour ce noeud
     */
    public String DrawableLabel();
    
    /**
     * Renvoie la couleur (pour pouvoir faire des red-black tree)
     * Si on ne fait pas de red-black, il suffit de toujours renvoyer true
     */
    public boolean DrawableRed();
}