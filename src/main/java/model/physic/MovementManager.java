package model.physic;

import model.board.*;
import model.entity.*;

public class MovementManager {
    private Board board;

    public MovementManager(Board board) {
        this.board = board;
    }

    
    // Tente de déplacer un agent dans une direction donnée.
     
    public void moveAgent(Agent agent, Direction dir) {
        // L'agent tourne toujours vers la direction demandée
        agent.setFacing(dir);

        // Calcul de la destination de l'agent
        Position currentPos = agent.getPos();
        int nextX = currentPos.getX() + dir.getDx();
        int nextY = currentPos.getY() + dir.getDy();

        // On vérifie si cet Item est traversable 
        if (canMoveTo(nextY, nextY)) {
        
            // Si l'agent porte un bloc, on vérifie aussi la case du bloc (nextBlockX/Y)
            if (agent.isCarrying()) {
                int nextBlockX = nextX + dir.getDx();
                int nextBlockY = nextY + dir.getDy();
            
                // On vérifie les limites pour le bloc
                if (!canMoveTo(nextBlockX, nextBlockY)) {
                    return;
                } 
            }

            // on éxecute le mouvement
            agent.setPosition(nextX, nextY);

            Items groundItem = board.getItems().getItem(nextY, nextX);
            // Puisque groundItem est de type Items, il possède onSteppedOn
            groundItem.onSteppedOn(agent);
        }
        
    }

    // Action de ramasser (GRAB) le bloc devant l'agent.
    
    public void grabBlock(Agent agent) {
        if (agent.isCarrying()) return; // Déjà occupé

        Position targetPos = getPositionInFront(agent);

        MovableEntity target = board.getEntityAt(targetPos.getX(), targetPos.getY());

        if (target instanceof Block) {
            agent.hold((Block) target);
            board.getMovableEntities().remove(target);
        }
    }

    
    // Action de poser (DROP) le bloc devant l'agent.
    
    public void dropBlock(Agent agent) {
        if (!agent.isCarrying()) return;

        Position dropPos = getPositionInFront(agent);

        // Est-ce que la case devant est libre et dans la carte ?
        if (canMoveTo(dropPos.getX(), dropPos.getY())) {
            Block b = agent.drop();
            b.setPosition(dropPos.getX(), dropPos.getY());

            // 2. On enregistre que le bloc occupe maintenant cet espace sur le plateau
            board.getMovableEntities().add(b);

            // On regarde ce qu'il y a "sous" le bloc dans la matrice
            Items groundBelow = board.getItems().getItem(dropPos.getY(), dropPos.getX());
            // On prévient l'item
            groundBelow.onSteppedOn(b);
        }
    }

    
    // Méthode de vérification centrale 
    
    public boolean canMoveTo(int x, int y) {
        // Vérification des limites de la matrice
        if (!board.getItems().isInside(y, x)) {
            return false; // Bloqué au bord
        }

        // Récupération de l'entité sur la case
        Items target = board.getItems().getItem(y, x);

        // Vérification des ponts / obstacles via CellType
        if (!target.isTraversable()) {
            return false;
        }

        // Vérifier si un objet mobile (Bloc/Robot) bloque le passage
        if (board.getEntityAt(x, y) != null) {
            return false; 
        }

        return true;
    }

    private Position getPositionInFront(Agent a) {
        return new Position(
            a.getPos().getX() + a.getFacing().getDx(),
            a.getPos().getY() + a.getFacing().getDy()
        );
    }
}
