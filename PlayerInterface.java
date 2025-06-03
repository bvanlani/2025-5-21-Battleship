import java.rmi.*;

public interface PlayerInterface extends Remote {
    void receivePlayerHit(int x, int y) throws RemoteException;
    void receiveTargetHit(int x, int y) throws RemoteException;
    void initializeFleet() throws RemoteException;
    boolean getIsReady() throws RemoteException;
    void addBoardEventListeners() throws RemoteException;
    String getPlayerSquareType(int x, int y) throws RemoteException;
    void setTargetSquareType(int x, int y, String type) throws RemoteException;
    void sinkBattleship(int length, Direction dir, int x, int y) throws RemoteException;
    int fleetSize() throws RemoteException;
    String getName() throws RemoteException;
    void setIsReady(boolean ready) throws RemoteException;
}
