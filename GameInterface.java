package dev.bvanlani.battleship;

import java.rmi.*;

public interface GameInterface extends Remote {
    Player getCurrentPlayer() throws RemoteException;
    void callHit(int row, int col) throws RemoteException;
    void sinkBattleship(Player player, int length, Direction dir, int x, int y) throws RemoteException;
    void checkWin() throws RemoteException;
    void addPlayer(Player player) throws RemoteException;
}
