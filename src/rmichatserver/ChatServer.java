/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rmichatserver;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

/**
 *
 * @author anatide
 */
public class ChatServer {
    
    private static final int PORT = 1099;
    private static final String SERVICE_NAME = "ChatService";
    
    public static void main(String[] args) {
        try {
            System.out.println("=================================");
            System.out.println("   Serveur de Chat RMI");
            System.out.println("=================================");
            
            // Créer le service de chat
            ChatServiceImpl chatService = new ChatServiceImpl();
            
            // Créer le registre RMI
            Registry registry = LocateRegistry.createRegistry(PORT);
            
            // Enregistrer le service
            registry.rebind(SERVICE_NAME, chatService);
            
            System.out.println("\n✓ Serveur démarré avec succès!");
            System.out.println("✓ Port: " + PORT);
            System.out.println("✓ Service: " + SERVICE_NAME);
            System.out.println("\nEn attente de connexions...");
            System.out.println("\nCommandes disponibles:");
            System.out.println("  - 'stats' : Afficher les statistiques");
            System.out.println("  - 'users' : Liste des utilisateurs connectés");
            System.out.println("  - 'exit'  : Arrêter le serveur");
            System.out.println("=================================\n");
            
            // Boucle de commandes serveur
            Scanner scanner = new Scanner(System.in);
            boolean running = true;
            
            while (running) {
                System.out.print("Serveur> ");
                String command = scanner.nextLine().trim().toLowerCase();
                
                switch (command) {
                    case "stats":
                        System.out.println("\n--- Statistiques du serveur ---");
                        System.out.println("Clients connectés: " + chatService.getClientCount());
                        System.out.println("Port: " + PORT);
                        System.out.println("------------------------------\n");
                        break;
                        
                    case "users":
                        System.out.println("\n--- Utilisateurs connectés ---");
                        if (chatService.getClientCount() == 0) {
                            System.out.println("Aucun utilisateur connecté");
                        } else {
                            for (String user : chatService.getOnlineUsers()) {
                                System.out.println("  - " + user);
                            }
                        }
                        System.out.println("------------------------------\n");
                        break;
                        
                    case "exit":
                        System.out.println("\nArrêt du serveur...");
                        running = false;
                        break;
                        
                    case "":
                        break;
                        
                    default:
                        System.out.println("Commande inconnue: " + command);
                        System.out.println("Commandes: stats, users, exit\n");
                }
            }
            
            scanner.close();
            System.out.println("Serveur arrêté.");
            System.exit(0);
            
        } catch (Exception e) {
            System.err.println("Erreur du serveur: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}