# DrawTheTree

![DrawTheTree](/DrawTheTree/tree.png?raw=true "DrawTheTree")

DrawTheTree est un package Java qui permet d'afficher une représentation d'un arbre (au plus binaire). Son intérêt réside dans le fait qu'il peut facilement se greffer sur n'importe quelle implémentation d'arbre (red-black, binaire, ...). 

L'implémentation n'est probablement ni la plus efficace ni la plus propre mais son objectif était d'avoir un package que l'on peut facilement venir greffer sur une implémentation d'arbre sans devoir aller modifier le code partout dans le package ou que l'arbre soit implémenté d'une certaine manière.

## Screenshots

Voici une capture d'écran de la fenêtre ainsi que d'un exemple d'image d'arbre qu'il est possible de générer avec ce package.

![Screenshot](/screenshot.png?raw=true "Screenshot")

![Sample](/sample.png?raw=true "Sample")

## Disclaimer

Ce package est un petit projet réalisé rapidement pour répondre à un besoin (visualiser un arbre pour débugguer un projet). N'ayant pas trouvé de solution que je pouvais facilement insérer dans ma structure de données, j'ai décidé d'écrire mon propre code. Et tant qu'à faire, autant le faire de manière le plus générique possible, pour pouvoir le réutiliser ou l'adapter à d'autres implémentations d'arbres. Après je me suis dit que si il m'étais utile, il pouvais peut-être l'être pour d'autres... Ce projet n'a pas la prétention d'être la solution ultime et complète, il a juste pour objectif de pouvoir facilement ajouter la possibilité de voir son implémentation d'arbre de manière visuelle.

## Installation

Pour ajouter le package dans un projet Java, il suffit de mettre le dossier DrawTheTree dans le projet puis de l'importer dans son IDE.

## Prérequis

Pour fonctionner, il faut que votre arbre respecte certaines conditions et une certaine structure. Ce package a été calculé de telle manière à ce que cette structure soit le moins contraignant possible.

* Votre arbre peut au plus être binaire (donc soit une liste soit binaire)
* Il peut avoir des liens de deux couleurs
* Il doit être implémenté dans une classe
* Dans cette classe, on a un noeud racine
* On a une classe qui représente un noeud

## Utilisation

**Importation du package**

Importer le package dans toutes les classes nécessaires

    import DrawTheTree.*;

**Implémenter DrawableTree**

Pour que votre classe qui crée votre arbre puisse être affichée il faut qu'elle implémente l'interface 'DrawableTree'.

    implements DrawableTree

L'interface 'DrawableTree' requiert d'implémenter certaines méthodes en plus dans la classe de votre arbre. Ces méthodes sont probablement déjà implémentées dans votre classe mais pour ne pas devoir modifier votre code ni celui du package pour faire en sorte que les noms des méthodes correspondent, on va ajouter quelques méthodes spécifiques qui vont simplement renvoyer le résultat que vos méthoes renvoyent.

Voici les méthodes à implémenter

    // Renvoie la taille de l'arbre
    public int size();

    // Renvoie true si l'arbre est vide
    public boolean isEmpty();
    
    // Renvoie la hauteur de l'arbre
    public int DrawableHeight();
    
    // Renvoie le noeud qui représente la racine de l'arbre
    public DrawableNode DrawableRoot();

La méthode qui renvoie la hauteur de l'arbre ne doit pas obligatoirement être exacte. Elle permets juste de calculer la taille de l'image à générer. Si vous n'avez pas de méthode qui permet de le calculer précisément, renvoyer une valeur plafond (= qui est forcément plus grande que la hauteur réelle)

**Implémenter DrawableNode**

Il faut ensuite étendre la classe qui représente chaque noeud de votre arbre. Pour cela, il faut d'abord que cette classe implémente 'DrawableNode'
    implements DrawableNode
    
Les méthodes qui doivent être implémentées sont les suivantes :

    // Renvoie l'enfant de gauche
    public DrawableNode DrawableLeft();
    
    // Renvoie l'enfant de droite
    public DrawableNode DrawableRight();

    // Renvoie un string (c'est ce qui sera affiché dans l'arbre pour ce noeud)
    public String DrawableLabel();
    
    // Renvoie true si le lien vers ce noeud est rouge
    // (si vous ne faites pas de red-black, vous pouvez simplement faire un 'return false;' dans tous les cas)
    public boolean DrawableRed();


## Affichage de mes arbres

Vient maintenant la partie la plus chouette, l'affichage des arbre. Pour cela, plusieurs manières.

**Depuis l'extérieur**

On crée notre arbre puis on crée un objet DrawTree pour l'afficher.

    SearchTree st = new SearchTree();
        
    DrawTree dt = new DrawTree(st);
        
**Depuis une méthode dans notre classe**

    public class SearchTree implements DrawableTree {
      // [...]
    
      public DrawTree drawer; 
    
      public void showTree() {
          drawer = new DrawTree(this);
      }
    
      public int DrawableHeight() {
          // [...]
      }
    
      // [...]
      
Et on affiche alors notre arbre ainsi :

    SearchTree st = new SearchTree();
        
    st.showTree();
    
**Directement dans le constructeur**

On peut également directement afficher l'arbre lorsqu'il est construit

    public class SearchTree implements DrawableTree {
      // [...]
    
      public DrawTree drawer;
      
      public SearchTree() {
          // [...]
          
          drawer = new DrawTree(this);
      }
    
      // [...]

## Méthodes de l'objet DrawTree

On peut appeler certaines méthodes sur notre objet 'DrawTree' (indépendamment de comment on l'a créé)

**refresh()**

Permet de raffraichir l'arbre. Si on crée l'objet 'DrawTree' directement dans notre implémentation d'arbre, on peut par exemple exécuter cette méthde après chaque opération de modification (put, remove, ...)

    public class SearchTree implements DrawableTree {
      // [...]
    
     
      public void remove(Key key) {
          // [...]
          
          drawer.refresh();
      }
    
      // [...]
      
**addInfo(String info)**
 
Permet d'ajouter une ligne d'info dans la console de gauche sur la fenêtre d'affichage de l'arbre.

**saveImage(String file)**

Enregistre l'arbre dans une image png, dont le chemin est passé en argument. 

La méthode n'est pas défensive et elle ne va pas vérifier si c'est bien un png toussa toussa

**setNodeWidth(int width) et setNodeHeight(int height)**
 
Permet de modifier respectivement la largeur et la hauteur des noeuds dans l'affichage. Si l'argument est -1, il ne va rien modifier et juste renvoyer la valeur actuelle.
 
## To Do
 
Voici quelques améliorations à apporter. Il s'agit plutot d'un pense bête pour moi, mais si certains veulent d'amuser à améliorer le code, c'est avec plaisir :)

* Ajouter des settings dans la fenetre
* Ajouter des setters
* Scroll dans la console
* Vérifier l'utilité de tous les import
* Faire des arbres 2-3
* setColors
* Pouvoir créer le truc sans forcément l'afficher (par exemple si tu faux juste générer une image)
