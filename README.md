
# 📝 Todo List Backend - Java Spring Boot

Bienvenue dans le backend de mon application **Todo List** développée en **Java Spring Boot**. Ce projet implémente une architecture propre en suivant les principes **SOLID** et utilise des **Design Patterns** pour assurer une extensibilité et une maintenabilité exemplaires. 🚀

## 🧰 Technologies utilisées
- **Java 17** (version LTS recommandée)
- **Spring Boot** - Pour la rapidité du développement côté backend
- **Gradle (Groovy)** - Pour la gestion des dépendances
- **MySQL** - Pour la base de données relationnelle
- **JUnit** - Pour les tests unitaires et la méthode de développement **Test Driven Development (TDD)**

## 🎯 Fonctionnalités principales
- **Créer, Modifier, Supprimer et Valider** des notes.
- Utilisation du **Pattern Factory** pour créer dynamiquement des types de notes.
- Gestion complète des utilisateurs et des listes de notes.

## 💡 Architecture du projet

J'ai utilisé une approche modulaire et respecté les principes **SOLID** :

- **S**ingle Responsibility: Chaque classe a une seule responsabilité bien définie.
- **O**pen/Closed: Le système est ouvert à l'extension avec de nouveaux types de notes sans toucher au code existant.
- **L**iskov Substitution: Toutes les classes qui implémentent `Note` sont interchangeables.
- **I**nterface Segregation: L'interface `Note` est précise et claire, sans méthode inutile.
- **D**ependency Inversion: Nous dépendons d'abstractions (interfaces), pas d'implémentations concrètes.

## 📦 Structure des classes

Voici un aperçu de la structure des principales classes dans ce backend :

### `User` 👤
- Variables :
  - `id` (Long)
  - `username` (String)
  - `listNotes` (List<ListNote>)

### `ListNote` 📋
- Variables :
  - `id` (Long)
  - `notes` (List<Note>)
- Méthodes :
  - `addNote()` : Ajouter une nouvelle note
  - `removeNote()` : Supprimer une note
  - `getNotes()` : Récupérer toutes les notes

### `Note` (class abstraite) 📝
- Méthodes :
  - `getTitle()` : Retourne le titre de la note
  - `getContent()` : Retourne le contenu de la note
  - `getCreationDate()` : Retourne la date de création

### `TextNote` 📝✏️
- Variables :
  - `title` (String)
  - `creationDate` (LocalDateTime)

### `LongTextNote` 📝✅
- Variables :
  - `title` (String)
  - `content` (String)
  - `creationDate` (LocalDateTime)

### `NoteFactory` 🏭
- Méthode :
  - `createNote()` : Crée dynamiquement une nouvelle note en fonction du type
  - `modifyNote()` : Modifie dynamiquement une note en fonction du type (le type ne change pas, à corriger)

### `ListNoteFactory` 🏭
- Méthode :
  - `createListNote()` : Crée dynamiquement une nouvelle liste

## ⚙️ Design Pattern utilisé

- **Factory Pattern** : Pour gérer la création de notes via la classe `NoteFactory` sans exposer la logique interne.

## 🛠️ Test Driven Development (TDD)

J'utilise **JUnit** pour écrire des tests unitaires et vérifier que chaque fonctionnalité du backend fonctionne comme prévu. Cela permet de s'assurer que chaque module respecte bien son contrat.

### Tests sur les Controllers :

1. **TestUserController** :
  - Vérifie la création d'un utilisateur.
  - Vérifie la récupération d'un utilisateur par ID et par nom d'utilisateur.
  - Vérifie la suppression d'un utilisateur.

2. **TestListNoteController** :
  - Vérifie la création d'une liste de notes.
  - Vérifie l'ajout, la modification et la suppression de notes dans une liste spécifique.
  - Vérifie la récupération de toutes les notes et d'une note spécifique par ID.

### Exemple de tests :

```java
@Test
public void testCreateUser() throws Exception {
    User user = new User("john_doe");
    mockMvc.perform(post("/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(user)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.username").value("john_doe"));
}

@Test
public void testAddNoteToList() throws Exception {
    Note note = new TextNote("Meeting", LocalDateTime.now());
    mockMvc.perform(post("/lists/{userId}/{listId}", 1L, 1L)
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(note)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.notes.size()").value(1));
}
```

## 📂 Démarrage du projet

1. Clonez ce repository :
   ```bash
   git clone git@github.com:byborh/lge-da4-todolist-backend.git
   ```
2. Naviguez dans le dossier :
   ```bash
   cd todolist-backend
   ```
3. Lancez l'application avec **Gradle** :
   ```bash
   ./gradlew bootRun
   ```

## 📌 Conclusion

Ce backend a été conçu dans un souci de **propreté du code** et de **maintenabilité**. Grâce à l'application des **principes SOLID** et des **Design Patterns**, il est facile d'ajouter de nouvelles fonctionnalités sans toucher au code existant. 🎉

Contribuez et améliorez cette application pour l'adapter à vos besoins futurs ! 😎
