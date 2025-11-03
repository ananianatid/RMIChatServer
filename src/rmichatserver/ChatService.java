/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package rmichatserver;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 *
 * @author anatide
 */
public interface ChatService extends Remote {
    
    /**
     * Enregistre un nouveau client dans le chat
     * @param username Nom d'utilisateur
     * @param clientCallback Interface callback du client
     * @return true si l'enregistrement réussit
     * @throws RemoteException
     */
    boolean register(String username, ChatClientCallback clientCallback) throws RemoteException;
    
    /**
     * Déconnecte un client du chat
     * @param username Nom d'utilisateur à déconnecter
     * @throws RemoteException
     */
    void unregister(String username) throws RemoteException;
    
    /**
     * Envoie un message à tous les clients connectés
     * @param username Expéditeur du message
     * @param message Contenu du message
     * @throws RemoteException
     */
    void sendMessage(String username, String message) throws RemoteException;
    
    /**
     * Envoie un message privé à un utilisateur spécifique
     * @param from Expéditeur
     * @param to Destinataire
     * @param message Contenu du message
     * @throws RemoteException
     */
    void sendPrivateMessage(String from, String to, String message) throws RemoteException;
    
    /**
     * Récupère la liste des utilisateurs connectés
     * @return Liste des noms d'utilisateurs
     * @throws RemoteException
     */
    List<String> getOnlineUsers() throws RemoteException;
}
