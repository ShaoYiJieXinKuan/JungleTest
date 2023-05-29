package listener;

import model.ChessboardPoint;
import view.CellComponent;
import view.TotalChessComponent;

import java.io.IOException;

public interface GameListener {

    void onPlayerClickCell(ChessboardPoint point, CellComponent component) throws IOException;


    void onPlayerClickChessPiece(ChessboardPoint point, TotalChessComponent component) throws IOException;

}
