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

```java
import DrawTheTree.*;
```

**Implémenter DrawableTree**

Pour que votre classe qui crée votre arbre puisse être affichée il faut qu'elle implémente l'interface 'DrawableTree'.

```java
public class ExempleTree implements DrawableTree {
```

L'interface 'DrawableTree' requiert d'implémenter certaines méthodes en plus dans la classe de votre arbre. Ces méthodes sont probablement déjà implémentées dans votre classe mais pour ne pas devoir modifier votre code ni celui du package pour faire en sorte que les noms des méthodes correspondent, on va ajouter quelques méthodes spécifiques qui vont simplement renvoyer le résultat que vos méthoes renvoyent.

Voici les méthodes à implémenter

```java
// Renvoie la taille de l'arbre
public int DrawableSize();
    
// Renvoie la hauteur de l'arbre
public int DrawableHeight();
    
// Renvoie le noeud qui représente la racine de l'arbre
public DrawableNode DrawableRoot();
```

Par exemple, si votre implémentation contient une méthode ´size()´ qui renvoie la taille de l'arbre, vous pouvez simplement implémenter ´DrawableSize()´ de cette manière :

```java
public int DrawableSize() { return size(); }
```

La méthode qui renvoie la hauteur de l'arbre ne doit pas obligatoirement être exacte. Elle permets juste de calculer la taille de l'image à générer. Si vous n'avez pas de méthode qui permet de le calculer précisément, renvoyez une valeur plafond (= qui est forcément plus grande que la hauteur réelle mais le plus proche possible).

**Implémenter DrawableNode**

Il faut ensuite étendre la classe qui représente chaque noeud de votre arbre. Pour cela, il faut d'abord que cette classe implémente 'DrawableNode'

```java
public class ExempleNode implements DrawableNode {
	// [...]
}
```
    
Les méthodes qui doivent être implémentées sont les suivantes :

```java
// Renvoie l'enfant de gauche
public DrawableNode DrawableLeft();

// Renvoie l'enfant de droite
public DrawableNode DrawableRight();

// Renvoie un string (c'est ce qui sera affiché dans l'arbre pour ce noeud)
public String DrawableLabel();

// Renvoie true si le lien vers ce noeud est rouge
// (si vous ne faites pas de red-black, vous pouvez simplement faire un 'return false;' dans tous les cas)
public boolean DrawableRed();
```

A nouveau, si vous avez déjà une méthode qui fait cela, vous pouvez simplement renvoyer le résultat de cette méthode.

## Affichage des arbres

Vient maintenant la partie la plus chouette, l'affichage des arbre. Pour cela, plusieurs manières.

**Depuis l'extérieur**

On crée notre arbre puis on crée un objet DrawTree pour l'afficher. Cet exemple se trouve dans la main de ExempleTree.

```java
ExempleTree et = new ExempleTree();
 
DrawTree dt = new DrawTree(et);
```

**Depuis une méthode dans notre classe**

```java
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
}
``` 
     
Et on affiche alors notre arbre ainsi :

```java
SearchTree st = new SearchTree();

st.showTree();
```
    
**Directement dans le constructeur**

On peut également directement afficher l'arbre lorsqu'il est construit

```java
public class SearchTree implements DrawableTree {
	// [...]

	public DrawTree drawer;
  
	public SearchTree() {
		// [...]
	  
		drawer = new DrawTree(this);
	}

	// [...]
}
```

## Le constructeur de DrawTree

Ici au dessus nous avons vu le constructeur de DrawTree sous sa forme la plus simple : avec comme seul argument l'arbre qu'on veut afficher.

En réalité, il est pobbible de lui passer deux argument supplémentaires facultatifs :

```java
DrawTree(DrawableTree tree, boolean transparent, boolean youWantTheView)
```

* `boolean transparent` : si il vaut true, les images affichées et exportées en png seront transparentes (sans fond) [par défaut: false]
* `boolean youWantTheView` : si il vaut true, une fenêtre avec l'arbre sera affichée et si il vaut false il créera seulement l'objet [par défaut: true]

Mettre `youWantTheView` à false peut être intéressant si on veut par exemple juste utiliser DrawTree pour générer des images png de nos arbres.

On pourra alors simplement appeller la méthode permettant de générer les images png (voir en détail plus loin) :

```java
ExempleTree et = new ExempleTree();

DrawTree dt = new DrawTree(et, true, false);

// [...]

dt.saveImage("C:\\export.png");
```

## Méthodes de l'objet DrawTree

On peut appeler certaines méthodes sur notre objet `DrawTree` (indépendamment de comment et où on l'a créé)

**refresh()**

Permet de raffraichir l'arbre. Si on crée l'objet 'DrawTree' directement dans notre implémentation d'arbre, on peut par exemple exécuter cette méthde après chaque opération de modification (put, remove, ...)

```java
public class ExempleTree implements DrawableTree {
	// [...]
     
	public void remove(Key key) {
		// [...]
          
		drawer.refresh();
	}
    
	// [...]
}
```
  
**addInfo(String info)**
 
Permet d'ajouter une ligne d'info dans la console de gauche sur la fenêtre d'affichage de l'arbre.

**saveImage(String file)**

Enregistre l'arbre dans une image png, dont le chemin est passé en argument. 

La méthode n'est pas défensive et elle ne va pas vérifier si c'est bien un png toussa toussa !

saveImage() ne fait pas appel à refresh avant d'enregistrer l'image. Il faut donc d'abord appeler refresh si il y a des changements qui n'ont pas encore subi refresh().

**setNodeWidth(int width) et setNodeHeight(int height)**
 
Permet de modifier respectivement la largeur et la hauteur des noeuds dans l'affichage. Si l'argument est -1, il ne va rien modifier et juste renvoyer la valeur actuelle.

**Personnaliser les couleurs**

Il existe trois méthodes pour changer les couleurs utilisées pour les images.

Pour chaque méthode, si l'argument est null, la valeur actuelle sera renvoyée sans rien changer.

```java
setBkgColor (Color newColor); // Couleur de font
setDefaultColor (Color newColor); // Couleur d'écriture par défaut
setAltColor (Color newColor); // Couleur d'écriture secondaire (utilisée pour les liens rouges dans les red-black)
```

## ExempleTree

Une classe ExempleTree se trouve à la racine pour montrer le fonctionnement de ce package. Cette classe ne représente pas un "vrai arbre", c'est simplement 3 noeuds créés manuellement et liés pour montrer comment utiliser le package. A vous de l'adapter à vos besoins.

Pour le lancer, il suffit d'exécuter sa méthode main.

## To Do
 
Voici quelques améliorations à apporter. Il s'agit plutot d'un pense bête pour moi, mais si certains veulent d'amuser à améliorer le code, c'est avec plaisir :)

* Ajouter des settings dans la fenetre
* Ajouter des setters
* Scroll dans la console
* Vérifier l'utilité de tous les import
* Faire des arbres 2-3
* Appeler refresh avant de saver une image
* Pouvoir exporter une image transparent