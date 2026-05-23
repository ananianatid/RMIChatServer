# RMIChatServer

Application de chat multi-utilisateur utilisant Java RMI (Remote Method Invocation). Supporte l'enregistrement, la diffusion de messages, les messages prives et la liste des utilisateurs connectes.

## Stack technique

- Java
- Java RMI
- Ant / NetBeans

## Etat d'avancement

Prototype / incomplet -- le service et l'implementation sont fonctionnels mais le point d'entree principal est un stub TODO et il n'y a pas d'interface client.

## Demarrage

```bash
ant
```
Puis demarrer `rmiregistry` et executer `ChatServer`.
