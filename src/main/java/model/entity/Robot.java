package model.entity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import model.board.Board;
import model.command.Command;
import model.command.DropAtCommand;
import model.command.PickUpCommand;
import model.command.StopCommand;
import model.physic.Direction;
import model.physic.MovementManager;
import model.physic.Position;

public class Robot extends Agent {
    
    // Tableau fixe de 10 commandes maximum
    private Command[] program = new Command[10]; 
    private int addIndex = 0;  // Index pour ajouter une commande
    private int execIndex = 0; // Index de la commande en cours d'exécution
    
    private List<Direction> currentPath = null;               
    private boolean isExecuting = false;

    public Robot(Position pos) {
        super(pos);
    }

    // Ajoute une commande au tableau. max 9 pour garder la place du StopCommand
    public void addCommand(Command cmd) {
        if (addIndex < 9) {
            program[addIndex] = cmd;
            addIndex++;
        }
    }

    // Démarre l'exécution et ajoute le symbole de fin
    public void startExecution() {
        program[addIndex] = new StopCommand(); // Insère la commande d'arrêt
        execIndex = 0;                         // Commence à l'index 0
        isExecuting = true;
        currentPath = null;
    }

    // Stoppe l'exécution, vide le programme et annule les commandes restantes
    public void stopExecution() {
        isExecuting = false;
        currentPath = null;
        addIndex = 0; 
        program = new Command[10]; // Réinitialise la liste des commandes
    }

    public boolean isExecuting() {
        return isExecuting;
    }

    // Exécute une étape logique du robot
    public void executeStep(MovementManager moveMgr, Board board) {
        if (!isExecuting) return;

        Command currentCmd = program[execIndex];

        // Condition d'arrêt
        if (currentCmd instanceof StopCommand) {
            System.out.println("Robot : Fin de l'exécution du programme.");
            stopExecution();
            return;
        }

        // PHASE 1 : verification des conditions avant planifier le chemin 
        if (currentPath == null) {
            
            // Vérification des pré-conditions avant même de bouger
            if (currentCmd instanceof PickUpCommand) {
                if (this.isCarrying()) {
                    failAndStop("Erreur (PickUp) : Le robot porte déjà un bloc");
                    return;
                }
            } else if (currentCmd instanceof DropAtCommand) {
                if (!this.isCarrying()) {
                    failAndStop("Erreur (DropAt) : Le robot ne porte aucun bloc à déposer");
                    return;
                }
            }

            // Résoudre la position cible
            Position targetPos = resolveTargetPosition(currentCmd, board);
            if (targetPos == null) {
                failAndStop("Erreur : La cible est introuvable sur le plateau");
                return;
            }

            // Calcul du chemin le plus court
            boolean needsAdjacent = (currentCmd instanceof PickUpCommand || currentCmd instanceof DropAtCommand);
            currentPath = findShortestPath(moveMgr, board, this.getPos(), targetPos, needsAdjacent);
            
            if (currentPath == null) {
                failAndStop("Erreur : Aucun chemin possible vers la cible");
                return;
            }
        }

        // PHASE 2 : Déplacement suivant le chemin généré 
        if (!currentPath.isEmpty()) {
            Direction nextStep = currentPath.get(0);
            int ni = this.getPos().getI() + nextStep.getDi();
            int nj = this.getPos().getJ() + nextStep.getDj();

            if (moveMgr.canMoveTo(ni, nj)) {
                this.setFacing(nextStep);
                moveMgr.moveAgent(this, nextStep);
                currentPath.remove(0); 
            } else {
                currentPath = null; 
                // Si un obstacle est apparu, on recalculera le chemin au tour suivant
            }
            return; // Le déplacement prend un tour complet
        }

        //PHASE 3 : Vérification finale et Action (Le robot est arrivé) ---
        Position targetPos = resolveTargetPosition(currentCmd, board);
        if (targetPos == null) {
            failAndStop("Erreur : La cible a disparu pendant le trajet");
            return;
        }

        if (currentCmd instanceof PickUpCommand) {
            faceTarget(targetPos);
            // Vérifier s'il y a bien un bloc à ramasser
            MovableEntity entityToGrab = board.getEntityAt(targetPos.getI(), targetPos.getJ());
            if (!(entityToGrab instanceof Block)) {
                failAndStop("Erreur (PickUp) : Il n'y a pas ou plus de bloc sur la case cible !");
                return;
            }
            moveMgr.grabBlock(this); // Action réussie
        } 
        else if (currentCmd instanceof DropAtCommand) {
            faceTarget(targetPos);
            // Vérifier si la case cible est déjà occupée par une autre entité mobile
            MovableEntity entityAtTarget = board.getEntityAt(targetPos.getI(), targetPos.getJ());
            if (entityAtTarget != null) {
                failAndStop("Erreur (DropAt) : L'emplacement cible est déjà occupé !");
                return;
            }
            moveMgr.dropBlock(this); // Action réussie
        }
        
        // Si on arrive ici, la commande est officiellement accomplie. On passe à la suivante.
        execIndex++;
        currentPath = null; 
    }

    // Résout l'emplacement physique de la cible selon son type
    private Position resolveTargetPosition(Command cmd, Board board) {
        if (cmd.getEntityTarget() != null) {
            return cmd.getEntityTarget().getPos();
        } else if (cmd.getInfraTarget() != null) {
            for (int i = 0; i < board.getNbLines(); i++) {
                for (int j = 0; j < board.getNbColumns(); j++) {
                    if (board.getItemAt(i, j) == cmd.getInfraTarget()) {
                        return new Position(i, j);
                    }
                }
            }
        }
        return null;
    }

    // Affiche l'erreur dans la console et stoppe immédiatement le robot
    private void failAndStop(String errorMsg) {
        System.out.println(errorMsg); 
        stopExecution();
    }

    // Force le robot à se tourner vers la case cible pour ramasser/poser
    private void faceTarget(Position p) {
        int di = p.getI() - this.getPos().getI();
        int dj = p.getJ() - this.getPos().getJ();
        if (di > 0) setFacing(Direction.DOWN);
        else if (di < 0) setFacing(Direction.UP);
        else if (dj > 0) setFacing(Direction.RIGHT);
        else if (dj < 0) setFacing(Direction.LEFT);
    }

    // recherche du plus court chemin par BFS 
    private List<Direction> findShortestPath(MovementManager moveMgr, Board board, Position start, Position target, boolean adjacentOnly) {
        boolean[][] visited = new boolean[board.getNbLines()][board.getNbColumns()];
        Queue<PathNode> queue = new LinkedList<>();
        queue.add(new PathNode(start.getI(), start.getJ(), new ArrayList<>()));
        visited[start.getI()][start.getJ()] = true;

        if (adjacentOnly && isAdjacent(start, target)) return new ArrayList<>();

        while (!queue.isEmpty()) {
            PathNode curr = queue.poll();
            
            if (adjacentOnly) { 
                if (Math.abs(curr.i - target.getI()) + Math.abs(curr.j - target.getJ()) == 1) return curr.path;
            } else { 
                if (curr.i == target.getI() && curr.j == target.getJ()) return curr.path;
            }

            for (Direction dir : Direction.values()) {
                int ni = curr.i + dir.getDi();
                int nj = curr.j + dir.getDj();
                
                if (board.isInside(ni, nj) && !visited[ni][nj]) {
                    // Ne pas marcher SUR la case cible si on veut juste interagir avec (PickUp/DropAt)
                    if (adjacentOnly && ni == target.getI() && nj == target.getJ()) continue;
                    
                    if (moveMgr.canMoveTo(ni, nj)) {
                        visited[ni][nj] = true;
                        List<Direction> newPath = new ArrayList<>(curr.path);
                        newPath.add(dir);
                        queue.add(new PathNode(ni, nj, newPath));
                    }
                }
            }
        }
        return null;
    }

    // helper - Vérifier si deux cases sont adjacentes 
    private boolean isAdjacent(Position p1, Position p2) {
        return Math.abs(p1.getI() - p2.getI()) + Math.abs(p1.getJ() - p2.getJ()) == 1;
    }

    // helper - Classe interne pour mémoriser les nœuds du BFS
    private static class PathNode {
        int i, j;
        List<Direction> path;
        PathNode(int i, int j, List<Direction> path) {
            this.i = i;
            this.j = j;
            this.path = path;
        }
    }
}