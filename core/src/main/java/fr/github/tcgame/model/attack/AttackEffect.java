package fr.github.tcgame.model.attack;

public enum AttackEffect {
    NONE,
    DEAL_CRYSTAL_DMG,   // Inflige des degats directs au cristal (Ganon - Calamite)
    SELF_DMG,           // La carte s'inflige des degats (Electricien - Court-circuit)
    STEAL_COINS,        // Vole des pieces a l'adversaire (Huissier - Sommation)
    BONUS_COINS,        // Rapporte des pieces en plus si kill (Stonks - To the Moon)
    MISS_CHANCE,        // Prochaine attaque ennemie a X% de rater (Corbeau - Augure)
    EXTRA_ATTACK,       // Peut refaire une attaque normale (Leclerc - Pas de strategie)
    FORCE_SWAP,         // Force l'adversaire a echanger sa carte (Panzoli - Chaos Immersif)
    TRAP,               // Piege : explose si l'adversaire change de carte (Steve - TNT)
    POISON,             // Inflige X degats par tour tant que la carte est en jeu (Modo)
    PROTECT_CRYSTAL,    // Si la carte meurt, le cristal ne perd pas de PV (Assureur)
    FINAL_SENTENCE,     // Utilisable seulement si la cible a moins de 50% PV (Bourreau)
    NO_DRAW,            // L'adversaire ne peut pas piocher au prochain tour (Panzoli passif)
    REDUCE_DEPLOY_COST  // Reduit le cout du prochain deploiement de 1 (Actionnaire)
}
