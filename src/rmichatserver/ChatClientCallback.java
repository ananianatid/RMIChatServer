/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package rmichatserver;

import java.rmi.Remote;
import java.rmi.RemoteException;
/**
 *
 * @author anatide
 */
public interface ChatClientCallback extends Remote {
    
    /**
     * Reçoit un message du serveur
     * @param message Message à afficher
     * @throws RemoteException
     */
    void receiveMessage(String message) throws RemoteException;
    
    /**
     * Notification de mise à jour de la liste des utilisateurs
     * @param users Liste des utilisateurs connectés
     * @throws RemoteException
     */
    void updateUserList(String users) throws RemoteException;
}
