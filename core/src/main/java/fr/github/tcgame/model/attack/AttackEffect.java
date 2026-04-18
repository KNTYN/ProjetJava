package fr.github.tcgame.model.attack;

public enum AttackEffect {
    NONE,               // Pas d'effet
    DEAL_CRYSTAL_DMG,   // Inflige des degats au cristal adverse
    SELF_DMG,           // La carte s'inflige des degats a elle-meme
    STEAL_COINS,        // Vole des pieces a l'adversaire
    BONUS_COINS,        // Rapporte des pieces en plus si kill
    MISS_CHANCE,        // La prochaine attaque ennemie a X% de rater
    EXTRA_ATTACK,       // Peut refaire une attaque normale
    FORCE_SWAP,         // Force l'adversaire a echanger sa carte en jeu
    TRAP,               // Pose un piege, explose si l'adversaire change de carte
    POISON,             // Inflige des degats par tour
    PROTECT_CRYSTAL,    // Le cristal ne perd pas de PV si la carte meurt
    FINAL_SENTENCE,     // Utilisable seulement si la cible a moins de 50% PV
    NO_DRAW,            // L'adversaire ne peut pas piocher au prochain tour
    REDUCE_DEPLOY_COST  // Reduit le cout du prochain deploiement

}
