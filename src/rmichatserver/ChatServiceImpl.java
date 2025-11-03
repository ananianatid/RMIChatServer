/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rmichatserver;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
/**
 *
 * @author anatide
 */
public class ChatServiceImpl extends UnicastRemoteObject implements ChatService {
    
    // Map thread-safe pour stocker les clients connectés
    private final Map<String, ChatClientCallback> clients;
    private final SimpleDateFormat timeFormat;
    
    public ChatServiceImpl() throws RemoteException {
        super();
        this.clients = new ConcurrentHashMap<>();
        this.timeFormat = new SimpleDateFormat("HH:mm:ss");
    }
    
    @Override
    public synchronized boolean register(String username, ChatClientCallback clientCallback) throws RemoteException {
        if (username == null || username.trim().isEmpty()) {
            return false;
        }
        
        if (clients.containsKey(username)) {
            return false; // Nom d'utilisateur déjà pris
        }
        
        clients.put(username, clientCallback);
        System.out.println("[" + getCurrentTime() + "] Nouvel utilisateur: " + username);
        
        // Notifier tous les clients de la nouvelle connexion
        broadcastSystemMessage(username + " a rejoint le chat");
        notifyUserListUpdate();
        
        return true;
    }
    
    @Override
    public synchronized void unregister(String username) throws RemoteException {
        if (clients.remove(username) != null) {
            System.out.println("[" + getCurrentTime() + "] Utilisateur déconnecté: " + username);
            broadcastSystemMessage(username + " a quitté le chat");
            notifyUserListUpdate();
        }
    }
    
    @Override
    public void sendMessage(String username, String message) throws RemoteException {
        if (message == null || message.trim().isEmpty()) {
            return;
        }
        
        String formattedMessage = "[" + getCurrentTime() + "] " + username + ": " + message;
        System.out.println(formattedMessage);
        
        // Envoyer le message à tous les clients
        List<String> disconnectedUsers = new ArrayList<>();
        
        for (Map.Entry<String, ChatClientCallback> entry : clients.entrySet()) {
            try {
                entry.getValue().receiveMessage(formattedMessage);
            } catch (RemoteException e) {
                System.err.println("Erreur d'envoi à " + entry.getKey() + ": " + e.getMessage());
                disconnectedUsers.add(entry.getKey());
            }
        }
        
        // Retirer les clients déconnectés
        for (String user : disconnectedUsers) {
            clients.remove(user);
            System.out.println("[" + getCurrentTime() + "] Client déconnecté automatiquement: " + user);
        }
        
        if (!disconnectedUsers.isEmpty()) {
            notifyUserListUpdate();
        }
    }
    
    @Override
    public void sendPrivateMessage(String from, String to, String message) throws RemoteException {
        if (message == null || message.trim().isEmpty()) {
            return;
        }
        
        ChatClientCallback recipient = clients.get(to);
        ChatClientCallback sender = clients.get(from);
        
        if (recipient == null) {
            if (sender != null) {
                sender.receiveMessage("[SYSTÈME] Utilisateur " + to + " introuvable");
            }
            return;
        }
        
        String privateMessage = "[" + getCurrentTime() + "] [PRIVÉ] " + from + " -> " + to + ": " + message;
        System.out.println(privateMessage);
        
        try {
            recipient.receiveMessage(privateMessage);
            if (sender != null && !from.equals(to)) {
                sender.receiveMessage(privateMessage);
            }
        } catch (RemoteException e) {
            System.err.println("Erreur d'envoi du message privé: " + e.getMessage());
            clients.remove(to);
            notifyUserListUpdate();
        }
    }
    
    @Override
    public List<String> getOnlineUsers() throws RemoteException {
        return new ArrayList<>(clients.keySet());
    }
    
    /**
     * Envoie un message système à tous les clients
     */
    private void broadcastSystemMessage(String message) {
        String systemMessage = "[" + getCurrentTime() + "] [SYSTÈME] " + message;
        System.out.println(systemMessage);
        
        List<String> disconnectedUsers = new ArrayList<>();
        
        for (Map.Entry<String, ChatClientCallback> entry : clients.entrySet()) {
            try {
                entry.getValue().receiveMessage(systemMessage);
            } catch (RemoteException e) {
                disconnectedUsers.add(entry.getKey());
            }
        }
        
        for (String user : disconnectedUsers) {
            clients.remove(user);
        }
    }
    
    /**
     * Notifie tous les clients de la mise à jour de la liste des utilisateurs
     */
    private void notifyUserListUpdate() {
        String userList = String.join(", ", clients.keySet());
        List<String> disconnectedUsers = new ArrayList<>();
        
        for (Map.Entry<String, ChatClientCallback> entry : clients.entrySet()) {
            try {
                entry.getValue().updateUserList(userList);
            } catch (RemoteException e) {
                disconnectedUsers.add(entry.getKey());
            }
        }
        
        for (String user : disconnectedUsers) {
            clients.remove(user);
        }
    }
    
    /**
     * Retourne l'heure actuelle formatée
     */
    private String getCurrentTime() {
        return timeFormat.format(new Date());
    }
    
    /**
     * Retourne le nombre de clients connectés
     */
    public int getClientCount() {
        return clients.size();
    }
}