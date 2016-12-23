# DrawTheTree

![DrawTheTree](/DrawTheTree/tree.png?raw=true "DrawTheTree" =250x)

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
* Il faut avoir un noeud racine et une classe qui représente un noeud

## Utilisation

**Importation du package**

Importer le package dans toutes les classes nécessaires :

```java
import DrawTheTree.*;
```

**Implémenter DrawableNode**

Il faut ensuite modifier la classe qui représente un noeud de votre arbre. Pour cela, il faut d'abord que cette classe implémente `DrawableNode` :

```java
public class ExempleNode implements DrawableNode {
	// [...]
}
```
    
Les méthodes qui doivent ensuite être implémentées sont les suivantes :

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

Si vous avez déjà une méthode qui fait une de ces opérations, vous pouvez simplement renvoyer le résultat de votre méthode dans la méthode a implémenter. Si vous avez par exemple une méthode `getTheLabel()`, vous pouvez implémenter `DrawableLabel()` de cette manière :

```java
public String DrawableLabel() {
	return getTheLabel();
}
```

## Construire un DrawTree

Vient maintenant la partie la plus chouette, l'affichage des arbres. Pour cela, il suffit de créer un objet `DrawTree`.

Le constructeur de `DrawTree` prends comme premier paramètre un `DrawableNode` qui représente la racine de l'arbre.

Il est aussi pobbible de lui passer trois arguments supplémentaires facultatifs. 

```java
DrawTree(DrawableTree tree, boolean youWantTheView, HashMap<String, Comparable> settings)
```

* `boolean youWantTheView` : si il vaut true, une fenêtre avec l'arbre sera affichée et si il vaut false il créera seulement l'objet [par défaut: true]
* `HashMap<String, Comparable> settings` : un `HashMap` de paramètres pour modifier les paramètres par défaut (voir la liste des paramètres qu'on peut modifier plus bas)

Mettre `youWantTheView` à `false` peut être intéressant si on veut par exemple juste utiliser `DrawTree` pour générer des images png de nos arbres.

On pourra alors simplement appeller la méthode permettant de générer les images png (voir en détail plus loin) :

```java
ExempleTree et = new ExempleTree();

DrawTree dt = new DrawTree(et, true);

// [...]

dt.saveImage("C:\\export.png");
```


## Comment créer le constructeur

On peut créer le contructeur à plusieurs endroits différents :

**Depuis l'extérieur**

On crée notre arbre normalement puis on crée un objet `DrawTree` pour l'afficher. On trouve un exemple de cette technique dans `ExempleTreeExt`.

```java
ExempleTreeExt et = new ExempleTreeExt();

DrawTree dt = new DrawTree(et.root);
```

**Depuis une méthode dans notre classe**

On peut aussi créer une méthode spécifique dans l'implémentation qui va créer un `DrawTree` et l'afficher. On trouve un exemple de cette technique dans `ExempleTreeMeth`.

```java
public class ExempleTreeMeth implements DrawableTree {
	// [...]

	public DrawTree dt; 

	public void showTree() {
		dt = new DrawTree(this);
	}

	public int DrawableHeight() {
		// [...]
	}

	// [...]
}
``` 
     
Et on affiche alors notre arbre ainsi :

```java
ExempleTreeMeth et = new ExempleTreeMeth();

et.showTree();
```
    
**Directement dans le constructeur**

On peut également directement afficher l'arbre lorsqu'il est construit. On trouve un exemple de cette technique dans `ExempleTreeInt`.

```java
public class ExempleTreeInt implements DrawableTree {
	// [...]

	public DrawTree dt;
  
	public ExempleTreeInt() {
		// [...]
	  
		dt = new DrawTree(this);
	}

	// [...]
}
```



## Méthodes de l'objet DrawTree

On peut appeler certaines méthodes sur notre objet `DrawTree` (indépendamment de comment et où on l'a créé)

**refresh()**

Permet de raffraichir l'arbre. Si on crée l'objet `DrawTree` directement dans notre implémentation d'arbre, on peut par exemple exécuter cette méthode après chaque opération de modification (`put`, `remove`, ...)

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
  
**saveImage(String file)**

Enregistre l'arbre dans une image png, dont le chemin est passé en argument. 

La méthode n'est pas défensive et elle ne va pas vérifier si c'est bien un png toussa toussa !

`saveImage()` ne fait pas appel à `refresh()` avant d'enregistrer l'image. Il faut donc d'abord appeler `refresh()` si il y a des changements qui ont été faits.

**setSetting(String key, Object value) et setSettings(HashMap<String, Object> newSettings)**

Ces deux méthodes permettent de modifier un ou des paramètres par défaut. Pour modifier plusieurs paramètres on les place dans une HashMap.

Les paramètres qu'on peut modifier sont :

* `fontSize` [type: int], [défaut: 15] : taille de la police
* `nodeWidth` [type: int], [défaut: 80] : largueur d'un noeud
* `nodePadding` [type: int], [défaut: 3] : padding intérieur de chaque noeud
* `nodeXmargin` [type: int], [défaut: 4] :  marge à gauche et à droite de chaque noeud
* `transparentBkg` [type: boolean], [défaut: false] :  arrière plan transparent ou non
* `colorBkg` [type: Color], [défaut: blanc] : couleur de fond de l'image
* `colorDefault` [type: Color], [défaut: noir] : couleur par défaut
* `colorAlt` [type: Color], [défaut: rouge] : couleur secondaire (utilisée pour les liens rouges dans les red-black)

**getSetting(String key)**

Cette méthode permets de récupérer la valeur actuelle du setting passé en argument.

## ExempleTree

Trois classes exemple se trouvent à la racine pour montrer le fonctionnement de ce package. Ces classes ne représentent pas un *"vrai arbre"*, ce sont simplement 3 noeuds créés manuellement et liés pour montrer comment utiliser le package. A vous de les adapter à vos besoins.

Pour voir les exemples, il suffit d'exécuter leur méthode `main`.

## To Do
 
Voici quelques améliorations à apporter. Il s'agit plutot d'un pense bête pour moi, mais si certains veulent d'amuser à améliorer le code, c'est avec plaisir :)

* Vérifier l'utilité de tous les import
* Faire des arbres 2-3
* Appeler refresh avant de saver une image