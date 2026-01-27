# VERSION PREVIEW
Cette version est pour but de vérifier des idées ou des implémentations basiques en visualisant leurs effets.  

## Configuration
- Le jeu est implémenté dans un projet mvn.  
- Téléchargez d'abord votre code à local avec git.  
- Ensuite vous devez exécuter la commande `mvn clean javafx:run` pour installer javafx.  
- Quand tout est prêt, exécuter `Launcher.java`

## Éléments et règles du jeu 
- Un agent qui représente le joueur, manipulable.
- Des régions non connectées. 
- L'agent ne peut se déplacer que dans les limites d'une région, si elle est pas connectée à une autre région.
- Des blocs portables.
- Un bloc ne peut être placé sur une position disponible cad où il n'y a pas d'objet.
- Des triggers. Quand un objet est placé sur le trigger, il fait un point entre deux régions. Quand la position est vide, le point est récupéré et disparaitre.  

## Mode d'emploi
- `WASD` pour déplacer l'agent
- `SPACE` pour porter/placer un objet dans la direction du déplacement le plus récent de l'agent 

## Réalisation future 
- Un point de fin: si l'agent l'atteint alors le joueur a gagné et le jeu se termine
- Terrains différents
- Le robot 
- Le terminal 






