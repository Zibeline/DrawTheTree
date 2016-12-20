package DrawTheTree;

/**
 * Interface des méthdes que l'implémentation de l'arbre doit avoir
 * 
 * Documentation complète disponible sur GitHub : https://github.com/Zibeline/DrawTheTree
 * 
 * @author DenisM
 * @version Décembre 2016
 */
public interface DrawableTree {
    /**
     * Renvoie la taille de l'arbre
     */
    public int DrawableSize();
    
    /**
     * Renvoie la hauteur de l'arbre
     */
    public int DrawableHeight();
    
    /**
     * Renvoie un node (qui imlémente DrawableNode) de la racine de l'arbre
     */
    public DrawableNode DrawableRoot();
}