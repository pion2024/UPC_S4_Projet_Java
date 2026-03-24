package model.physic;

import model.board.Board;
import model.entity.Agent;
import model.entity.Block;
import model.entity.Items;
import model.entity.MovableEntity;

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
        int nextI = currentPos.getI() + dir.getDi();
        int nextJ = currentPos.getJ() + dir.getDj();

        // On vérifie si cet Item est traversable 
        if (canMoveTo(nextI, nextJ)) {


            // on éxecute le mouvement
            agent.setPosition(nextI, nextJ);

            Items groundItem = board.getElement(nextI, nextJ);
            // Puisque groundItem est de type Items, il possède onSteppedOn
            groundItem.onSteppedOn(agent);
        }
        
    }

    // Action de ramasser (GRAB) le bloc devant l'agent.
    
    public void grabBlock(Agent agent) {
        if (agent.isCarrying()) return; // Déjà occupé

        Position targetPos = getPositionInFront(agent);

        MovableEntity target = board.getEntityAt(targetPos.getI(), targetPos.getJ());

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
        if (canMoveTo(dropPos.getI(), dropPos.getJ())) {
            Block b = agent.drop();
            b.setPosition(dropPos.getI(), dropPos.getJ());

            // 2. On enregistre que le bloc occupe maintenant cet espace sur le plateau
            board.getMovableEntities().add(b);

            // On regarde ce qu'il y a "sous" le bloc dans la matrice
            Items groundBelow = board.getElement(dropPos.getI(), dropPos.getJ());
            // On prévient l'item
            groundBelow.onSteppedOn(b);
        }
    }

    
    // Méthode de vérification centrale 
    
    public boolean canMoveTo(int i, int j) {
        // Vérification des limites de la matrice
        if (!board.isInside(i, j)) {
            return false; // Bloqué au bord
        }

        // Récupération de l'entité sur la case
        Items target = board.getElement(i, j);

        // Vérification des ponts / obstacles via CellType
        if (!target.isTraversable()) {
            return false;
        }

        // Vérifier si un objet mobile (Bloc/Robot) bloque le passage
        if (board.getEntityAt(i, j) != null) {
            return false; 
        }

        return true;
    }

    private Position getPositionInFront(Agent a) {
        return new Position(
            a.getPos().getI() + a.getFacing().getDi(),
            a.getPos().getJ() + a.getFacing().getDj()
        );
    }
}
