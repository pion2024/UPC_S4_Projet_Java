package model;

import java.util.ArrayList;
import java.util.List;

import model.entity.*;

public class Board {
    Items[][] map;
    public Board(int nb_rows, int nb_cols) {
        initMap(nb_rows,nb_cols);
    }

    //init avec du ground partout 
    public void initMap(int nb_rows,int nb_cols){
        Items[][] map = new Items[nb_rows][nb_cols];
        for (Items[] line : map){
            for (Items item : line){
                item = new Ground();
            }
        }
    } 

    public Items getItemAt(int row, int colonne) {
        return map[row][colonne];
    }

    public Items[][] getMap(){
        return map;
    }
    
}
