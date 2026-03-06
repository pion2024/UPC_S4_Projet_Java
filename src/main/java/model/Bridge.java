// public class Bridge {
//     List<Switch> hostSwitches;
//     boolean isActivated;  
//     public void updateStatus() {
//         // on n'a besoin que d'implementer la logique "et" pour l'instant 
//         isActivated = true;
//         for (Switch sw : hostSwitches) {
//             if (!sw.isOperational()) {
//                 isActivated = false; // si un switch n'est pas opérationnel, le pont ne peut pas être activé
//                 return;
//             }
//         }
//     }
// }
// public class GameModel(){
//     List<Switch> switches;
//     List<Bridge> bridges;
    
//     while (true){  // la boucle du jeu, a modifier 
//         update(); // met à jour le statut de tous les ponts en fonction de l'état des switches
//         // autres logiques du jeu, comme vérifier les conditions de victoire, etc.
//     }
//     public void update() {
//         for (Bridge bridge : bridges) {
//             bridge.updateStatus(); // met à jour le statut de chaque pont en fonction de l'état des switches
//         }
//     }
// }