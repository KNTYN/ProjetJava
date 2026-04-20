# TCGame

---

# 🎮 Règles de jeu complètes

## 🧠 But du jeu

* Détruire le cristal/âme adverse via combat de cartes 1v1
* Situé derrière le plateau (comme dans Inscryption quand on attaque directement la personne en face)

---

## 🔁 Règles principales

### Tour de jeu

* Tour par tour avec actions simultanées :

  * Choisir l’action à faire
  * Les deux joueurs attaquent en même temps

### Zones de jeu

* Chaque joueur dispose de :

  * Une main
  * Un banc
  * Une place de jeu
* La main adverse n’est pas visible
* Le banc est visible

### Énergie

* +1 énergie par tour au début (puis s’incrémente)

### Pioche

* 1 pioche automatique par joueur au début du tour
* Pas d’autre pioche autorisée

### Économie

* Système de pièces
* Déploiement main → banc : coûte des pièces
* Déploiement banc → terrain : gratuit
* Échange carte terrain ↔ banc : coûte de l’énergie

---

## ⚔️ Règles de combat

### Système de base

* 1 carte contre 1 carte
* Chaque carte a :

  * Une défense (PV)
  * 2 attaques :

    * 1 normale
    * 1 spéciale (avec malus/effet)

### Mort

* Lorsqu'une carte meurt :

  * Sa vie est soustraite de la vie du cristal
  * Elle repart dans la pioche à la dernière position (TAD File)

### Victoire

* Cristal à 0 PV → fin de partie

### Coût des attaques

* Une attaque coûte de l’énergie
* Exception pour les petites cartes (début de jeu)

### Récompenses

* 1 carte battue = 1 pièce gagnée

---

## ⚙️ Constantes à définir

* nb de cartes sur le banc
* nb total de cartes dans la partie : 60
* nb de cartes rares/communes/heal dans la partie
* nb pv cristal
* quantité max énergie (barre graduée)
* nb de tours avant augmentation du gain d’énergie
* interval des attaques
* interval des défenses

---

## 🃏 Nombre de cartes

### Répartition

* 60 cartes dans la partie :

  * 36 monstres
  * 1 tasty crousty
  * 15 heal / sort / debuff appliqué au mob + cristal
  * 8 effets (couleurs / terrains / events)

---

## 🧬 Types de cartes

### Monstres

* Capitaliste

* Maudit

* Prodige

* Goofy

* Synergie possible entre types

---

### Sorts

* shuffle (rase tout le terrain)
* boost (bonus temporaire)
* freeze (empêche d’attaquer)
* soin (soigne le cristal)
* burst (dégâts aléatoires)
* total énergie (divise par 2)
* boomerang (renvoie au banc)
* raffinerie (converti énergie → dollar)
* pioche (pioche des cartes)
* urssaf (l'adversaire perd 50% de sa tune)

---

## 🧾 Cartes

### 💰 Capitaliste (fdp category)

#### Donald Trump (rare)

* 18 PV / Coût : 5 pièces
* Tweet impulsif (3) : 2 énergies
* Coup médiatique total (6) : 4 énergies / +1 prochaine attaque

#### Stonks (rare)

* 15 PV / Coût : 4 pièces
* Spéculation (3) : 2 énergies
* To the Moon (6) : 4 énergies / +3 pièces si kill

#### Huissier (commun)

* 12 PV / Coût : 3 pièces
* Saisie de biens (2) : 2 énergies
* Sommation de payer (4) : 4 énergies / -1 pièce adverse

#### Banquier (commun)

* 12 PV / Coût : 5 pièces
* Intérêts composés (3)
* Placement pas sécurisé (5) : +1 pièce

#### Actionnaire (commun)

* 10 PV / Coût : 2 pièces
* Dividende (2)
* Vote au conseil (4) : réduit coût banc

#### Assureur (commun)

* 13 PV / Coût : 3 pièces
* Petite ligne du contrat (3)
* Franchise élevée (5) : protège le cristal

---

### 😈 Maudit

#### Charles Leclerc (rare)

* 16 PV / Coût : 4 pièces
* Peut attaquer 2 fois

#### Ganon (rare)

* 20 PV / Coût : 5 pièces
* Dégâts directs au cristal

#### Bourreau (commun)

* 14 PV / Coût : 3 pièces
* Exécution si cible < 50%

#### Corbeau (commun)

* 9 PV / Coût : 2 pièces
* Chance d’annuler attaque

---

### 🧠 Prodige

#### Max Verstappen (rare)

#### Link (rare)

#### Mr Panzoli (légendaire)

* 18 PV / Coût : 6 pièces
* Force swap adverse
* Bloque la pioche

#### Steve (rare)

* 17 PV / Coût : 4 pièces
* Piège TNT

#### Leon Scott Kennedy (rare)

---

### 🤡 Goofy

#### Air Fryer (commun)

* 10 PV / Coût : 1 pièce
* Réduction dégâts

#### Électricien (commun)

* 8 PV / Coût : 2 pièces
* Dégâts + self damage

#### Autres

* Multiprise
* Amphithéâtre JJ 035
* Modo Discord (DOT)
* Demande de suivi Instagram
* Facture
* John
* Fiat Multipla (rare)

---

## 🖥️ Menus

### Menu principal

* Jouer :

  * Solo (choix difficulté)
  * Duel 1v1 local
* Bouton retour

### Paramètres

* Audio
* Contrôles
* Sauvegarder
* Default
* Retour

### Collection

* Barre de recherche
* Filtres
* Affichage stats

### Quitter

* Confirmation

---

## 🎮 En jeu

### Menu pause

* Reprendre
* Paramètres

### Quitter

* Menu principal
* Bureau

---

## 💻 Code

```java
Interface i = new Interface(1920,1080);
Menu menu = new Menu();
menu.addComponent(BUTTON,zdgbuzdgu);
menu.addComponent(SEARCH_BAR,zdgbuzdgu);

i.addMenu(menu);
i.fullScreent();
i.show();

Game game = new Game();
game.getTour(); 
game.getStatut(); // enum {EN COURS, TERMINE}

game.getClick();
```


A [libGDX](https://libgdx.com/) project generated with [gdx-liftoff](https://github.com/libgdx/gdx-liftoff).

This project was generated with a template including simple application launchers and an `ApplicationAdapter` extension that draws libGDX logo.

## Platforms

- `core`: Main module with the application logic shared by all platforms.
- `lwjgl3`: Primary desktop platform using LWJGL3; was called 'desktop' in older docs.

## Gradle

This project uses [Gradle](https://gradle.org/) to manage dependencies.
The Gradle wrapper was included, so you can run Gradle tasks using `gradlew.bat` or `./gradlew` commands.
Useful Gradle tasks and flags:

- `--continue`: when using this flag, errors will not stop the tasks from running.
- `--daemon`: thanks to this flag, Gradle daemon will be used to run chosen tasks.
- `--offline`: when using this flag, cached dependency archives will be used.
- `--refresh-dependencies`: this flag forces validation of all dependencies. Useful for snapshot versions.
- `build`: builds sources and archives of every project.
- `cleanEclipse`: removes Eclipse project data.
- `cleanIdea`: removes IntelliJ project data.
- `clean`: removes `build` folders, which store compiled classes and built archives.
- `eclipse`: generates Eclipse project data.
- `idea`: generates IntelliJ project data.
- `lwjgl3:jar`: builds application's runnable jar, which can be found at `lwjgl3/build/libs`.
- `lwjgl3:run`: starts the application.
- `test`: runs unit tests (if any).

Note that most tasks that are not specific to a single project can be run with `name:` prefix, where the `name` should be replaced with the ID of a specific project.
For example, `core:clean` removes `build` folder only from the `core` project.
