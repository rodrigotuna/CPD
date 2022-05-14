package server;

import java.io.File;
import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface MembershipInterface extends Remote {
    void join() throws IOException;

    void leave() throws RemoteException;

}
