package alquerque.control;

import alquerque.model.AlquerqueBoard;
import alquerque.model.AlquerquePawn;
import alquerque.model.AlquerqueStageModel;
import boardifier.control.Controller;
import boardifier.control.Decider;
import boardifier.model.Model;
import boardifier.model.action.ActionList;

import java.util.ArrayList;
import java.util.List;

public class AlquerqueDeciderBot3Jesus extends Decider {

    public AlquerqueDeciderBot3Jesus (Model model, Controller control) {
        super(model, control);
    }

    @Override
    public ActionList decide() {
        AlquerqueStageModel stage = (AlquerqueStageModel) model.getGameStage();
        AlquerqueBoard board = stage.getBoard();
        int botColor = model.getIdPlayer();  // color bot get
        boardToMatrix(board);

        // simulation of blows




        return null;
    }

    private int[][] boardToMatrix(AlquerqueBoard board) {
        int[][] matrix = new int[5][5];
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                Object e = board.getElement(row, col);
                if (e == null) {
                    matrix[row][col] = -1;   // vide
                } else {
                    AlquerquePawn p = (AlquerquePawn) e;
                    matrix[row][col] = p.getColor();   // 0 = blanc, 1 = noir
                }
            }
        }
        return matrix;
    }


    private int[][] copyMatrix(int[][] matrix) {
        int[][] copy = new int[5][5];
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                copy[row][col] = matrix[row][col];
            }
        }
        return copy;
    }







/**
    // Représentation matricielle du board
    private int[][] boardToMatrix(AlquerqueBoard board)

    // Copier une matrice (pour simuler sans modifier l'originale)
    private int[][] copyMatrix(int[][] matrix)

    // Lister tous les coups possibles pour une couleur donnée
    private List<int[]> getAllMoves(int[][] matrix, int color)
// Retourne des coups au format {rowStart, colStart, rowEnd, colEnd}

    // Appliquer un coup sur une matrice (sans la copier)
    private void applyMove(int[][] matrix, int[] move)

    // Évaluer une position pour le bot
    private int evaluatePosition(int[][] matrix, int myColor)
**/

}
