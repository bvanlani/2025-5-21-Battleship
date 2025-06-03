import java.rmi.*;

public interface GameInterface extends Remote {
    String getCurrentPlayer() throws RemoteException;
    void callHit(int row, int col) throws RemoteException;
    void sinkBattleship(PlayerInterface player, int length, Direction dir, int x, int y) throws RemoteException;
    void checkWin() throws RemoteException;
    void addPlayer(PlayerInterface player) throws RemoteException;
    void gameRun(PlayerInterface player) throws RemoteException;
}
