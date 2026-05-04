package model;

import model.board.Board;
import model.entity.Agent;
import model.entity.Block;
import model.entity.BlockSwitch;
import model.entity.Bridge;
import model.entity.Cable.Intersection;
import model.entity.Couple;
import model.entity.Ground;
import model.entity.InteractionSwitch;
import model.entity.PressureSwitch;
import model.entity.Robot;
import model.entity.Terminal;   
import model.physic.Direction;
import model.physic.Position;

public enum Level {
    LEVEL_1(10, 10) {
        @Override
        public void setup(GameModel model) {
            Board board = model.getBoard();

            // Création des îles de base
            createIsland(board, 0, 0, 4, 10); // bloc de gauche
            createIsland(board, 6, 0, 4, 10); // bloc de droite

            Terminal terminal = new Terminal();
            // Le joueur commence en (0,0), le terminal est placé en (0,1)
            board.setItem(0, 1, terminal); 

            Robot robot = new Robot(new Position(3, 3));
            model.getBoard().getMovableEntities().add(robot);

            // Switch
            BlockSwitch sw1 = new BlockSwitch(new Position(5, 2));
            board.setItem(5, 2, sw1);

            // Joueur
            Agent player = new Agent(new Position(0, 0));
            model.getBoard().getMovableEntities().add(player);
            model.setPlayer(player);

            // Bloc
            Block box = new Block(new Position(2, 2));
            model.getBoard().getMovableEntities().add(box);

            // Ponts
            Bridge b1 = new Bridge(2, new Position(5, 4), false, Direction.UP);
            Bridge b2 = new Bridge(3, new Position(5, 5), false, Direction.UP);
            b1.addSwitch(sw1);
            b2.addSwitch(sw1);
            board.setItem(5, 4, b1);
            board.setItem(5, 5, b2);


            // Connexions
            Couple c1 = new Couple(sw1, b1);
            Couple c2 = new Couple(sw1, b2);

            c1.connexion(board);
            c2.connexion(board);

            model.addCouple(c1);
            model.addCouple(c2);

        }
    },

    LEVEL_2(10, 10) {
        @Override
        public void setup(GameModel model) {
            Board board = model.getBoard();

            // Création des îles de base
            createIsland(board, 0, 0, 4, 10); // bloc de gauche
            createIsland(board, 6, 0, 4, 10); // bloc de droite

            Terminal terminal = new Terminal();
            // Le joueur commence en (0,0), le terminal est placé en (0,1)
            board.setItem(0, 1, terminal); 

            Robot robot = new Robot(new Position(0, 3));
            model.getBoard().getMovableEntities().add(robot);

            // Switch
            BlockSwitch sw1 = new BlockSwitch(new Position(4, 1));
            board.setItem(4, 1, sw1);

            PressureSwitch sw2 = new PressureSwitch(new Position(5, 2));
            board.setItem(5, 2, sw2);

            InteractionSwitch sw3 = new InteractionSwitch(new Position(6, 3));
            board.setItem(6, 3, sw3);

            // Joueur
            Agent player = new Agent(new Position(0, 0));
            model.getBoard().getMovableEntities().add(player);
            model.setPlayer(player);

            // Bloc
            Block box = new Block(new Position(1, 1));
            model.getBoard().getMovableEntities().add(box);

            Block box2 = new Block(new Position(2, 2));
            model.getBoard().getMovableEntities().add(box2);

            Block box3 = new Block(new Position(3, 3));
            model.getBoard().getMovableEntities().add(box3);

            // Ponts
            // pont pour le switch 1
            Bridge b1 = new Bridge(2, new Position(5, 4), false, Direction.UP);
            Bridge b2 = new Bridge(3, new Position(5, 5), false, Direction.UP);
            b1.addSwitch(sw1);
            b2.addSwitch(sw1);
            board.setItem(5, 4, b1);
            board.setItem(5, 5, b2);

            // pont pour le switch 2
            Bridge b3 = new Bridge(2, new Position(6, 4), false, Direction.UP);
            Bridge b4 = new Bridge(3, new Position(6, 5), false , Direction.UP);
            b3.addSwitch(sw2);
            b4.addSwitch(sw2);
            board.setItem(6, 4, b3);
            board.setItem(6, 5, b4);

            // pont pour le switch 3
            Bridge b5 = new Bridge(2, new Position(7, 4), false, Direction.UP);
            Bridge b6 = new Bridge(3, new Position(7, 5), false, Direction.UP);
            b5.addSwitch(sw3);
            b6.addSwitch(sw3);
            board.setItem(7, 4, b5);
            board.setItem(7, 5, b6);


            //Connexions
            Couple c1 = new Couple(sw1, b1);
            Couple c2 = new Couple(sw1, b2);

            c1.connexion(board);
            c2.connexion(board);

            model.addCouple(c1);
            model.addCouple(c2);

            Couple c3 = new Couple(sw2, b3);
            Couple c4 = new Couple(sw2, b4);

            c3.connexion(board);
            c4.connexion(board);

            model.addCouple(c3);
            model.addCouple(c4);

            Couple c5 = new Couple(sw3, b5);
            Couple c6 = new Couple(sw3, b6);

            c5.connexion(board);
            c6.connexion(board);

            model.addCouple(c5);
            model.addCouple(c6);
        }
    },

    LEVEL_3(10, 10) {
        @Override
        public void setup(GameModel model) {
            Board board = model.getBoard();

            // Création des îles de base
            createIsland(board, 0, 0, 4, 10); // bloc de gauche
            createIsland(board, 5, 0, 5, 10); // bloc de droite

            // Joueur
            Agent player = new Agent(new Position(0, 0));
            model.getBoard().getMovableEntities().add(player);
            model.setPlayer(player);

            // Bloc
            Block box = new Block(new Position(2, 2));
            model.getBoard().getMovableEntities().add(box);

            Block box2 = new Block(new Position(2, 3));
            model.getBoard().getMovableEntities().add(box2);

            // Switch
            BlockSwitch sw1 = new BlockSwitch(new Position(7, 1));
            board.setItem(7, 1, sw1);

            BlockSwitch sw2 = new BlockSwitch(new Position(3, 1));
            board.setItem(3, 1, sw2);

            // Ponts
            Bridge b1 = new Bridge(2, new Position(5, 4), false, Direction.UP);
            board.setItem(5, 4, b1);

            // Intersection
            Intersection inter1 = new Intersection(new Position(5, 2));
            board.setItem(5, 2, inter1);

            // Connexions
            Couple c1 = new Couple(sw1, inter1);
            Couple c2 = new Couple(sw2, inter1);
            Couple c3 = new Couple(inter1, b1);

            c1.connexion(board);
            c2.connexion(board);
            c3.connexion(board);

            inter1.addCable(c1.getLastCable());
            inter1.addCable(c2.getLastCable());

            model.addCouple(c1);
            model.addCouple(c2);
            model.addCouple(c3);
        }
    };


    private final int width;
    private final int height;

    Level(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth() { return width; }
    public int getHeight() { return height; }

    public abstract void setup(GameModel model);

    protected void createIsland(Board board, int startJ, int startI, int width, int height) {
        for (int i = startI; i < startI + height; i++) {
            for (int j = startJ; j < startJ + width; j++) {
                if (board.isInside(i, j)) {
                    board.setItem(i, j, new Ground());
                }
            }
        }
    }
}