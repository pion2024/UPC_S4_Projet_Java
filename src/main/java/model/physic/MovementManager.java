package model.physic;

import model.board.Board;
import model.entity.Agent;
import model.entity.ArrivalSwitch;
import model.entity.Block;
import model.entity.BlockSwitch;
import model.entity.InteractionSwitch;
import model.entity.Items;
import model.entity.MovableEntity;
import model.entity.PressureSwitch;
import model.entity.Switch;
import model.entity.Terminal;

public class MovementManager {
    private Board board;

    public MovementManager(Board board) {
        this.board = board;
    }

    
    // Tente de déplacer un agent dans une direction donnée.
     
    public void moveAgent(MovableEntity entity, Direction dir) {
        int oldI = entity.getPos().getI();
        int oldJ = entity.getPos().getJ();

        int newI = oldI + dir.getDi();
        int newJ = oldJ + dir.getDj();

        // Vérifier que la case est valide
        if (!board.isInside(newI, newJ)) {
            return;
        }

        Items targetItem = board.getItemAt(newI, newJ);

        if (targetItem instanceof BlockSwitch && !(entity instanceof Block) || targetItem instanceof InteractionSwitch || targetItem instanceof Terminal) {
            return; // joueur/robot ne peuvent pas passer
        }

        // Vérifier si traversable
        if (targetItem != null && !targetItem.isTraversable()) {
            return;
        }

        // Vérifier s'il y a déjà une entité
        if (board.getEntityAt(newI, newJ) != null) return;

        // QUITTER l'ancien switch
        Items oldItem = board.getItemAt(oldI, oldJ);
        if (oldItem instanceof Switch sw) {
            sw.onExit(entity);
        }

        // déplacer l'entité
        entity.setPosition(newI, newJ);

        // ENTRER sur le nouveau switch
        Items newItem = board.getItemAt(newI, newJ);
        if (newItem instanceof Switch sw) {
            sw.onSteppedOn(entity);
        }
        
    }

    // Action de ramasser (GRAB) le bloc devant l'agent.
    
    public void grabBlock(Agent agent) {
        if (agent.isCarrying()) return; // Déjà occupé

        Position targetPos = getPositionInFront(agent);
        int i = targetPos.getI();
        int j = targetPos.getJ();

        MovableEntity target = board.getEntityAt(targetPos.getI(), targetPos.getJ());

        if (target instanceof Block b) {
            // prévenir le switch AVANT de retirer le bloc
            Items item = board.getItemAt(i, j);
            if (item instanceof Switch sw) {
                sw.onExit(b);
            }
            agent.hold((Block) target);
            board.getMovableEntities().remove(target);
        }
    }

    
    // Action de poser (DROP) le bloc devant l'agent.
    
    public void dropBlock(Agent agent) {
        if (!agent.isCarrying()) return;

        Position dropPos = getPositionInFront(agent);
        int i = dropPos.getI();
        int j = dropPos.getJ();

        // Vérifie que la case est valide et libre
        if (!board.isInside(i, j)) return;

        Items itemAtPos = board.getItemAt(i, j);
        MovableEntity entityAtPos = board.getEntityAt(i, j);

        // Déjà occupé
        if (entityAtPos != null) return;
        if (itemAtPos instanceof PressureSwitch) return;

        if (itemAtPos instanceof Switch sw) {

            // si ce n’est pas un BlockSwitch → interdit
            if (!(sw instanceof BlockSwitch)) {
                return;
            }

        // BlockSwitch accepte → on continue sans vérifier traversable
        } else {
            // Cas normal (sol, pont, etc.)
            if (!itemAtPos.isTraversable()) return;
        }


        // Déposer le bloc
        Block b = agent.drop();
        b.setPosition(dropPos.getI(), dropPos.getJ());// le bloc connaît sa position
        board.getMovableEntities().add(b); // ajoute au board

        // Prévenir l'item dessous (ex: switch ou bridge)
        itemAtPos.onSteppedOn(b);
    }

    
    // Méthode de vérification centrale 
    
    public boolean canMoveTo(int i, int j) {
        // Vérification des limites de la matrice
        if (!board.isInside(i, j)) {
            return false; // Bloqué au bord
        }

        // Récupération de l'entité sur la case
        Items target = board.getItemAt(i, j);

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

    public void activeBlock(Agent a){
        // 1. Récupérer la position du joueur
        Position playerPos = a.getPos();
    
        // 2. Récupérer l'élément sur la case du joueur
        Items item = board.getItemAt(playerPos.getI(), playerPos.getJ());

        // 3. Vérifier le type et déclencher l'action
        if (item != null) {
            if (item instanceof ArrivalSwitch) {
                ((ArrivalSwitch) item).onInteract(a);
            } 
            else if (item instanceof InteractionSwitch) {
                ((InteractionSwitch) item).onInteract(a);
            }
        }
    }

    private Position getPositionInFront(Agent a) {
        return new Position(
            a.getPos().getI() + a.getFacing().getDi(),
            a.getPos().getJ() + a.getFacing().getDj()
        );
    }
}
